package com.st0x0ef.aitools.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class AIPickaxe extends Item {

    public AIPickaxe(Properties properties) {
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
        return state.is(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (level.isClientSide() || !state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
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
            List<ResourceLocation> blocksBroken = AIItemsUtils.mineRadius(level, pos, miningEntity, BlockTags.MINEABLE_WITH_PICKAXE, AIItemsUtils.getMiningRadiusLevel(stack));
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
