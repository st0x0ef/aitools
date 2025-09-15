package com.st0x0ef.aitools.common.registries;

import com.mojang.serialization.MapCodec;
import com.st0x0ef.aitools.AITools;
import com.st0x0ef.aitools.common.loot.AddItemToChestsModifier;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class LootModifiersRegistry {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, AITools.MODID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<AddItemToChestsModifier>> ADD_ITEM_TO_CHESTS = LOOT_MODIFIER_SERIALIZERS.register("add_item_to_chests", () -> AddItemToChestsModifier.CODEC);
}