package com.st0x0ef.aitools.common.items;

import com.st0x0ef.aitools.common.DataComponents.AIToolData;
import com.st0x0ef.aitools.common.registries.DataComponentsRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AIItemsUtils {
    public static float getMiningSpeedMultiplier(Map<ResourceLocation, Integer> blocksBrokenMap, ResourceLocation blockLocation) {
        int blocksBroken = blocksBrokenMap.getOrDefault(blockLocation, 0);

        float speedMultiplier;

        if (blocksBroken < 110) {
            speedMultiplier = 1f;
        } else if(blocksBroken < 500) {
            speedMultiplier = (float) blocksBroken / 390f + 28f/39f;
        } else {
            speedMultiplier = (float) Math.log10(blocksBroken - 400f);
        }

        return speedMultiplier;
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
            stack.set(DataComponentsRegistry.AI_TOOL_DATA.get(), new AIToolData(new HashMap<>(), 0));
            blocksBroken = new AIToolData(new HashMap<>(), 0);
        }
        return blocksBroken.blocksBrokenData();
    }

    public static int getFortuneLevel(ItemStack stack) {
        return stack.getOrDefault(DataComponentsRegistry.AI_TOOL_DATA.get(), new AIToolData(new HashMap<>(), 0)).fortuneLevel();
    }

    public static void createTooltip(ItemStack stack, List<Component> tooltipComponents) {
        int fortuneLevel = AIItemsUtils.getFortuneLevel(stack);
        if (fortuneLevel > 0) {
            tooltipComponents.add(Component.literal("Fortune level : " + fortuneLevel).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.BLUE));
        }

        Map<ResourceLocation, Integer> blocksBrokenMap = getBlocksBrokenMap(stack);

        if (!blocksBrokenMap.isEmpty()) {
            tooltipComponents.add(Component.literal("Blocks broken : ").withStyle(ChatFormatting.GRAY));

            AtomicInteger i = new AtomicInteger();
            blocksBrokenMap.forEach((location, amount) -> {
                if (i.incrementAndGet() <= 10) {
                    tooltipComponents.add(Component.literal(location.toString() + " x " + amount).withStyle(ChatFormatting.GRAY));
                }
            });
        }
    }
}
