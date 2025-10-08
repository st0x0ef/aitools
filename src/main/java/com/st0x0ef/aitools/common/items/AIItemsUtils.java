package com.st0x0ef.aitools.common.items;

import com.st0x0ef.aitools.common.components.AIToolData;
import com.st0x0ef.aitools.common.config.Config;
import com.st0x0ef.aitools.common.registries.DataComponentsRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class AIItemsUtils {
    public static float getMiningSpeedMultiplier(ItemStack stack, ResourceLocation blockLocation) {
        int blocksBroken = getBlocksBrokenMap(stack).getOrDefault(blockLocation, 0);

        float speedMultiplier;

        if (blocksBroken < 110) {
            speedMultiplier = 1f;
        } else if(blocksBroken < 500) {
            speedMultiplier = (float) blocksBroken / 390f + 28f/39f;
        } else {
            speedMultiplier = (float) Math.log10(blocksBroken - 400f);
        }

        speedMultiplier *= (1f + getEfficiencyLevel(stack) * 0.2f);

        return Math.min(speedMultiplier, Config.MAX_SPEED_MULTIPLIER.getAsInt());
    }

    public static void syncFortuneToVanillaEnchantments(ItemStack stack, HolderLookup.Provider registries) {
        int level = getFortuneLevel(stack);

        ItemEnchantments current = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(current);

        HolderLookup.RegistryLookup<Enchantment> registry = registries.lookupOrThrow(Registries.ENCHANTMENT);
        Holder.Reference<Enchantment> fortune = registry.getOrThrow(Enchantments.FORTUNE);

        if (level > 0) {
            mutable.set(fortune, level);
        } else {
            mutable.removeIf((enchantmentHolder) -> enchantmentHolder.equals(fortune));
        }

        stack.set(DataComponents.ENCHANTMENTS, mutable.toImmutable());
        stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
    }

    public static Map<ResourceLocation, Integer> getBlocksBrokenMap(ItemStack stack) {
        AIToolData blocksBroken = stack.get(DataComponentsRegistry.AI_TOOL_DATA.get());
        if (blocksBroken == null) {
            blocksBroken = AIToolData.empty();
            stack.set(DataComponentsRegistry.AI_TOOL_DATA.get(), blocksBroken);
        }
        return blocksBroken.blocksBrokenData();
    }

    public static int getFortuneLevel(ItemStack stack) {
        return Math.min(stack.getOrDefault(DataComponentsRegistry.AI_TOOL_DATA.get(), AIToolData.empty()).fortuneLevel(), Config.MAX_FORTUNE_LEVEL.getAsInt());
    }

    public static int getMiningRadiusLevel(ItemStack stack) {
        return Math.min(stack.getOrDefault(DataComponentsRegistry.AI_TOOL_DATA.get(), AIToolData.empty()).radiusLevel(), Config.MAX_RADIUS_LEVEL.getAsInt());
    }

    public static int getEfficiencyLevel(ItemStack stack) {
        return Math.min(stack.getOrDefault(DataComponentsRegistry.AI_TOOL_DATA.get(), AIToolData.empty()).efficiencyLevel(), Config.MAX_EFFICIENCY_LEVEL.getAsInt());
    }


    public static void createTooltip(ItemStack stack, Consumer<Component> tooltipComponents) {
        int fortuneLevel = AIItemsUtils.getFortuneLevel(stack);
        if (fortuneLevel > 0) {
            tooltipComponents.accept(Component.literal("Fortune level : " + fortuneLevel).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.LIGHT_PURPLE));
        }

        int radiusLevel = AIItemsUtils.getMiningRadiusLevel(stack);
        if (radiusLevel > 0) {
            tooltipComponents.accept(Component.literal("Mining radius : " + (radiusLevel * 2 + 1) + "x" + (radiusLevel * 2 + 1)).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED));
        }

        int efficiencyLevel = AIItemsUtils.getEfficiencyLevel(stack);
        if (efficiencyLevel > 0) {
            tooltipComponents.accept(Component.literal("Efficiency level : " + efficiencyLevel).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.GREEN));
        }

        Map<ResourceLocation, Integer> blocksBrokenMap = getBlocksBrokenMap(stack);

        if (!blocksBrokenMap.isEmpty()) {
            tooltipComponents.accept(Component.literal("Blocks broken : ").withStyle(ChatFormatting.GRAY));

            AtomicInteger i = new AtomicInteger();
            blocksBrokenMap.forEach((location, amount) -> {
                if (i.incrementAndGet() <= 10) {
                    tooltipComponents.accept(Component.literal(location.toString() + " x " + amount).withStyle(ChatFormatting.GRAY));
                }
            });
        }
    }


    public static List<ResourceLocation> mineRadius(Level level, BlockPos origin, Entity entity, TagKey<Block> mineableTag, int radius) {
        Direction faceDir = entity.getDirection();
        Direction.Axis axis = faceDir.getAxis();

        List<ResourceLocation> blocksBroken = new ArrayList<>();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (!isInPlane(axis, dx, dy, dz)) continue;
                    if (dx == 0 && dy == 0 && dz == 0) continue;
                    BlockPos targetPos = origin.offset(dx, dy, dz);
                    BlockState targetState = level.getBlockState(targetPos);

                    if (targetState.isAir()) continue;
                    if (!targetState.is(mineableTag)) continue;
                    if (targetState.getDestroySpeed(level, targetPos) < 0) continue;

                    blocksBroken.add(BuiltInRegistries.BLOCK.getKey(targetState.getBlock()));
                    level.destroyBlock(targetPos, true, entity);
                }
            }
        }

        return blocksBroken;
    }

    public static boolean isInPlane(Direction.Axis axis, int dx, int dy, int dz) {
        return switch (axis) {
            case X -> dy != Integer.MIN_VALUE && dz != Integer.MIN_VALUE && dx == 0;
            case Y -> dx != Integer.MIN_VALUE && dz != Integer.MIN_VALUE && dy == 0;
            case Z -> dx != Integer.MIN_VALUE && dy != Integer.MIN_VALUE && dz == 0;
        };
    }

    public static int getRandomChangeToGetFortuneUpgrade(ItemStack stack) {
        int actualFortuneLevel = getFortuneLevel(stack);
        Map<ResourceLocation, Integer> blocksBrokenMap = getBlocksBrokenMap(stack);

        if (actualFortuneLevel >= Config.MAX_FORTUNE_LEVEL.getAsInt()) {
            return 0;
        }
        int numberOfBlocksBroken = blocksBrokenMap.values().stream().mapToInt(Integer::intValue).sum();
        double chance = Math.min(0.25f, (numberOfBlocksBroken / 10000f - actualFortuneLevel) * 0.01f);
        return Math.random() < chance ? 1 : 0;
    }

    public static int getRandomChangeToGetRadiusUpgrade(ItemStack stack) {
        int actualRadiusLevel = getMiningRadiusLevel(stack);
        Map<ResourceLocation, Integer> blocksBrokenMap = getBlocksBrokenMap(stack);

        if (actualRadiusLevel >= Config.MAX_RADIUS_LEVEL.getAsInt()) {
            return 0;
        }
        int numberOfBlocksBroken = blocksBrokenMap.values().stream().mapToInt(Integer::intValue).sum();
        double chance = Math.min(0.25f, (numberOfBlocksBroken / 10000f - actualRadiusLevel) * 0.01f);
        return Math.random() < chance ? 1 : 0;
    }

    public static int getRandomChangeToGetEfficiencyUpgrade(ItemStack stack) {
        int actualEfficiencyLevel = getEfficiencyLevel(stack);
        Map<ResourceLocation, Integer> blocksBrokenMap = getBlocksBrokenMap(stack);

        if (actualEfficiencyLevel >= Config.MAX_EFFICIENCY_LEVEL.getAsInt()) {
            return 0;
        }
        int numberOfBlocksBroken = blocksBrokenMap.values().stream().mapToInt(Integer::intValue).sum();
        double chance = Math.min(0.25f, (numberOfBlocksBroken / 10000f - actualEfficiencyLevel) * 0.01f);
        return Math.random() < chance ? 1 : 0;
    }

    public static void setData(ItemStack stack, Map<ResourceLocation, Integer> newBlocksBrokenMap) {
        stack.set(DataComponentsRegistry.AI_TOOL_DATA.get(), new AIToolData(newBlocksBrokenMap, AIItemsUtils.getFortuneLevel(stack) + AIItemsUtils.getRandomChangeToGetFortuneUpgrade(stack), AIItemsUtils.getMiningRadiusLevel(stack) + AIItemsUtils.getRandomChangeToGetRadiusUpgrade(stack), AIItemsUtils.getEfficiencyLevel(stack) + AIItemsUtils.getRandomChangeToGetEfficiencyUpgrade(stack)));
    }
}
