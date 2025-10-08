package com.st0x0ef.aitools.common.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.ItemAbilities;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class AIAxe extends Item {

    public AIAxe(Properties properties) {
        super(properties);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        ResourceLocation blockLocation = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        float miningSpeedMultiplier = AIItemsUtils.getMiningSpeedMultiplier(stack, blockLocation);
        return super.getDestroySpeed(stack, state) * miningSpeedMultiplier;
    }


    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return state.is(BlockTags.MINEABLE_WITH_AXE);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (level.isClientSide() || !state.is(BlockTags.MINEABLE_WITH_AXE)) {
            return true;
        }

        HashMap<ResourceLocation, Integer> blocksBrokenMap = new HashMap<>(AIItemsUtils.getBlocksBrokenMap(stack));
        ResourceLocation blockLocation = BuiltInRegistries.BLOCK.getKey(state.getBlock());

        if (blocksBrokenMap.containsKey(blockLocation)) {
            blocksBrokenMap.compute(blockLocation, (k, currentCount) -> currentCount + 1);
        } else {
            blocksBrokenMap.put(blockLocation, 1);
        }

        if (AIItemsUtils.getMiningRadiusLevel(stack) > 0) {
            List<ResourceLocation> blocksBroken = AIItemsUtils.mineRadius(level, pos, miningEntity, BlockTags.MINEABLE_WITH_AXE, AIItemsUtils.getMiningRadiusLevel(stack));
            for (ResourceLocation location : blocksBroken) {
                if (blocksBrokenMap.containsKey(location)) {
                    blocksBrokenMap.compute(location, (k, currentCount) -> currentCount + 1);
                } else {
                    blocksBrokenMap.put(location, 1);
                }
            }
        }

        AIItemsUtils.setData(stack, blocksBrokenMap);

        AIItemsUtils.syncFortuneToVanillaEnchantments(stack, level.registryAccess());
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Player player = context.getPlayer();
        if (playerHasBlockingItemUseIntent(context)) {
            return InteractionResult.PASS;
        } else {
            Optional<BlockState> optional = this.evaluateNewBlockState(level, blockpos, player, level.getBlockState(blockpos), context);
            if (optional.isEmpty()) {
                return InteractionResult.PASS;
            } else {
                ItemStack itemstack = context.getItemInHand();
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
                }

                level.setBlock(blockpos, optional.get(), 11);
                level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, (BlockState)optional.get()));
                if (player != null) {
                    itemstack.hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
                }

                return InteractionResult.SUCCESS;
            }
        }
    }

    private boolean playerHasBlockingItemUseIntent(UseOnContext context) {
        Player player = context.getPlayer();
        return context.getHand().equals(InteractionHand.MAIN_HAND) && player.getOffhandItem().has(DataComponents.BLOCKS_ATTACKS) && !player.isSecondaryUseActive();
    }

    private Optional<BlockState> evaluateNewBlockState(Level level, BlockPos pos, Player player, BlockState state, UseOnContext context) {
        Optional<BlockState> optional = Optional.ofNullable(state.getToolModifiedState(context, ItemAbilities.AXE_STRIP, false));
        if (optional.isPresent()) {
            level.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return optional;
        } else {
            Optional<BlockState> optional1 = Optional.ofNullable(state.getToolModifiedState(context, ItemAbilities.AXE_SCRAPE, false));
            if (optional1.isPresent()) {
                spawnSoundAndParticle(level, pos, player, state, SoundEvents.AXE_SCRAPE, 3005);
                return optional1;
            } else {
                Optional<BlockState> optional2 = Optional.ofNullable(state.getToolModifiedState(context, ItemAbilities.AXE_WAX_OFF, false));
                if (optional2.isPresent()) {
                    spawnSoundAndParticle(level, pos, player, state, SoundEvents.AXE_WAX_OFF, 3004);
                    return optional2;
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    private static void spawnSoundAndParticle(Level level, BlockPos pos, Player player, BlockState state, SoundEvent soundEvent, int p_450956_) {
        level.playSound(player, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.levelEvent(player, p_450956_, pos, 0);
        if (state.getBlock() instanceof ChestBlock && state.getValue(ChestBlock.TYPE) != ChestType.SINGLE) {
            BlockPos blockpos = ChestBlock.getConnectedBlockPos(pos, state);
            level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, level.getBlockState(blockpos)));
            level.levelEvent(player, p_450956_, blockpos, 0);
        }
    }

    @Override
    public int getEnchantmentLevel(ItemStack stack, Holder<Enchantment> enchantment) {
        if (enchantment.is(Enchantments.FORTUNE)) {
            return AIItemsUtils.getFortuneLevel(stack);
        }

        return 0;
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> component, TooltipFlag flag) {
        AIItemsUtils.createTooltip(stack, component);
    }
}
