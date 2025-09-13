package com.st0x0ef.aitools.common.registries;

import com.st0x0ef.aitools.AITools;
import com.st0x0ef.aitools.common.items.AIPickaxe;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemsRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AITools.MODID);

    // Tools
    public static final DeferredItem<Item> AI_PICKAXE = ITEMS.registerItem("ai_pickaxe", (p) -> new AIPickaxe(Tiers.STONE, p), new Item.Properties());

    // Block Items
    public static final DeferredItem<BlockItem> COMPUTER_ITEM = ITEMS.registerItem("computer", (p) -> new BlockItem(BlocksRegistry.COMPUTER.get(), p), new Item.Properties());
}
