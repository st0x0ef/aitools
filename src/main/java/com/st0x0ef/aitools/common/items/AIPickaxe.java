package com.st0x0ef.aitools.common.items;

import com.st0x0ef.aitools.AITools;
import com.st0x0ef.aitools.common.DataComponents.AIToolData;
import com.st0x0ef.aitools.common.registries.DataComponentsRegistry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
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

        stack.set(DataComponentsRegistry.AI_TOOL_DATA.get(), new AIToolData(blocksBrokenMap, getFortuneLevel(stack)));
        syncFortuneToVanillaEnchantments(stack, level.registryAccess());


        AITools.LOGGER.error("Blocks broken : ");
        blocksBrokenMap.forEach((key, value) -> AITools.LOGGER.error("{} : {}", key, value));

        return true;
    }

    @Override
    public int getEnchantmentLevel(ItemStack stack, Holder<Enchantment> enchantment) {
        if (enchantment.is(Enchantments.FORTUNE)) {
            return getFortuneLevel(stack);
        }

        return 0;
    }

    private void syncFortuneToVanillaEnchantments(ItemStack stack, HolderLookup.Provider registries) {
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

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    private Map<ResourceLocation, Integer> getBlocksBrokenMap(ItemStack stack) {
        AIToolData blocksBroken = stack.get(DataComponentsRegistry.AI_TOOL_DATA.get());
        if (blocksBroken == null) {
            stack.set(DataComponentsRegistry.AI_TOOL_DATA.get(), new AIToolData(new HashMap<>(), 0));
            blocksBroken = new AIToolData(new HashMap<>(), 0);
        }
        return blocksBroken.blocksBrokenData();
    }

    private int getFortuneLevel(ItemStack stack) {
        return stack.getOrDefault(DataComponentsRegistry.AI_TOOL_DATA.get(), new AIToolData(new HashMap<>(), 0)).fortuneLevel();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        int fortuneLevel = getFortuneLevel(stack);
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
