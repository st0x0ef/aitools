package com.st0x0ef.aitools.common.items;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class AIHoe extends Item {

    public AIHoe(Properties properties) {
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
        return state.is(BlockTags.MINEABLE_WITH_HOE);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (level.isClientSide() || !state.is(BlockTags.MINEABLE_WITH_HOE)) {
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
            List<ResourceLocation> blocksBroken = AIItemsUtils.mineRadius(level, pos, miningEntity, BlockTags.MINEABLE_WITH_HOE, AIItemsUtils.getMiningRadiusLevel(stack));
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

    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState toolModifiedState = level.getBlockState(blockpos).getToolModifiedState(context, ItemAbilities.HOE_TILL, false);
        Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = toolModifiedState == null ? null : Pair.of((Predicate<UseOnContext>)(ctx) -> true, HoeItem.changeIntoState(toolModifiedState));
        if (pair == null) {
            return InteractionResult.PASS;
        } else {
            Predicate<UseOnContext> predicate = pair.getFirst();
            Consumer<UseOnContext> consumer = pair.getSecond();
            if (predicate.test(context)) {
                Player player = context.getPlayer();
                level.playSound(player, blockpos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!level.isClientSide()) {
                    consumer.accept(context);
                    if (player != null) {
                        context.getItemInHand().hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
                    }
                }

                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
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
