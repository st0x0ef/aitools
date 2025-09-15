package com.st0x0ef.aitools.common.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.List;

public class AddItemToChestsModifier extends LootModifier {
    public static final MapCodec<AddItemToChestsModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            LootModifier.codecStart(inst)
                    .and(BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(m -> m.item))
                    .and(Codec.INT.fieldOf("min").forGetter(m -> m.min))
                    .and(Codec.INT.fieldOf("max").forGetter(m -> m.max))
                    .and(Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance))
                    .and(ResourceLocation.CODEC.listOf().fieldOf("tables").forGetter(m -> m.tables))
                    .apply(inst, AddItemToChestsModifier::new)
    );

    private final Item item;
    private final int min;
    private final int max;
    private final float chance;
    private final List<ResourceLocation> tables;

    public AddItemToChestsModifier(LootItemCondition[] conditions, Item item, int min, int max, float chance, List<ResourceLocation> tables) {
        super(conditions);
        this.item = item;
        this.min = min;
        this.max = max;
        this.chance = chance;
        this.tables = tables;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ResourceLocation tableId = context.getQueriedLootTableId();
        if (tables.contains(tableId) && context.getRandom().nextFloat() < chance) {
            int bound = Math.max(1, (max - min) + 1);
            int count = min + context.getRandom().nextInt(bound);
            if (count > 0) generatedLoot.add(new ItemStack(item, count));
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends LootModifier> codec() {
        return CODEC;
    }
}