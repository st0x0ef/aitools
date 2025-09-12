package com.st0x0ef.aitools.common.registries;

import com.st0x0ef.aitools.AITools;
import com.st0x0ef.aitools.common.items.AIPickaxe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemsRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AITools.MODID);

    public static final DeferredItem<Item> AI_PICKAXE = ITEMS.registerItem("ai_pickaxe", (p) -> new AIPickaxe(Tiers.STONE, p), new Item.Properties());

}
