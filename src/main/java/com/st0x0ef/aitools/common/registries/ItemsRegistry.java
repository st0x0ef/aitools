package com.st0x0ef.aitools.common.registries;

import com.st0x0ef.aitools.AITools;
import com.st0x0ef.aitools.common.components.AIToolData;
import com.st0x0ef.aitools.common.items.AIAxe;
import com.st0x0ef.aitools.common.items.AIPickaxe;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;

public class ItemsRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AITools.MODID);

    // Tools
    public static final DeferredItem<Item> AI_PICKAXE = ITEMS.registerItem("ai_pickaxe", p -> new AIPickaxe(Tiers.STONE, p), new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
    public static final DeferredItem<Item> AI_AXE = ITEMS.registerItem("ai_axe", p -> new AIAxe(Tiers.STONE, p), new Item.Properties().stacksTo(1).rarity(Rarity.RARE));

    // Upgrade Gems
    public static final DeferredItem<Item> FORTUNE_GEM = ITEMS.registerItem("fortune_gem", p -> new Item(p.component(DataComponentsRegistry.AI_TOOL_DATA, new AIToolData(new HashMap<>(), 1))), new Item.Properties());

    // Block Items
    public static final DeferredItem<BlockItem> COMPUTER_ITEM = ITEMS.registerItem("computer", p -> new BlockItem(BlocksRegistry.COMPUTER.get(), p), new Item.Properties());
}
