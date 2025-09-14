package com.st0x0ef.aitools.common.items;

import com.st0x0ef.aitools.AITools;
import com.st0x0ef.aitools.common.DataComponents.BlocksBrokenToolData;
import com.st0x0ef.aitools.common.registries.DataComponentsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AIPickaxe extends PickaxeItem {

    public AIPickaxe(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        ResourceLocation blockLocation = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        float miningSpeedMultiplier = ItemsUtils.getMiningSpeedMultiplier(getBlocksBrokenMap(stack), blockLocation);
        AITools.LOGGER.error(String.valueOf(miningSpeedMultiplier));
        return super.getDestroySpeed(stack, state) * miningSpeedMultiplier;
    }


    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return state.is(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (level.isClientSide || !state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
            return true;
        }

        HashMap<ResourceLocation, Integer> blocksBrokenMap = new HashMap<>(getBlocksBrokenMap(stack));
        ResourceLocation blockLocation = BuiltInRegistries.BLOCK.getKey(state.getBlock());

        if (blocksBrokenMap.containsKey(blockLocation)) {
            blocksBrokenMap.compute(blockLocation, (k, currentCount) -> currentCount + 1);
        } else {
            blocksBrokenMap.put(blockLocation, 1);
        }

        stack.set(DataComponentsRegistry.BLOCKS_BROKEN_MAP.get(), new BlocksBrokenToolData(blocksBrokenMap));

        AITools.LOGGER.error("Blocks broken : ");
        blocksBrokenMap.forEach((key, value) -> AITools.LOGGER.error("{} : {}", key, value));

        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    private Map<ResourceLocation, Integer> getBlocksBrokenMap(ItemStack stack) {
        BlocksBrokenToolData blocksBroken = stack.get(DataComponentsRegistry.BLOCKS_BROKEN_MAP.get());
        if (blocksBroken == null) {
            stack.set(DataComponentsRegistry.BLOCKS_BROKEN_MAP.get(), new BlocksBrokenToolData(new HashMap<>()));
            blocksBroken = new BlocksBrokenToolData(new HashMap<>());
        }
        return blocksBroken.blocksBrokenData();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("Stats : ").withColor(Color.gray.getRGB()));

        Map<ResourceLocation, Integer> blocksBrokenMap = getBlocksBrokenMap(stack);

        AtomicInteger i = new AtomicInteger();
        blocksBrokenMap.forEach((location, amount) -> {
            if (i.incrementAndGet() <= 10) {
                tooltipComponents.add(Component.literal(location.toString() + " x " + amount).withColor(Color.gray.getRGB()));
            }
        });
    }
}
