package com.st0x0ef.aitools.common.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.aitools.common.blocks.entities.ComputerBlockEntity;
import com.st0x0ef.aitools.common.registries.RecipesRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public record ComputerRecipe(List<Ingredient> recipeItems, ItemStack output) implements Recipe<ComputerInput> {
    public static RecipeType<?> TYPE = RecipesRegistry.COMPUTER_TYPE.get();

    @Override
    public boolean matches(ComputerInput container, Level level) {
        for (int i = 0; i < ((ComputerBlockEntity) container.entity()).getContainerSize() - 1; i++) {
            if (!recipeItems.get(i).test(container.getItem(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(ComputerInput container, HolderLookup.Provider provider) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return output;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.createWithCapacity(this.recipeItems.size());
        list.addAll(this.recipeItems);
        return list;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipesRegistry.COMPUTER_SERIALIZED.get();
    }

    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }

    public static class Serializer implements RecipeSerializer<ComputerRecipe> {

        public static final MapCodec<ComputerRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.listOf(1, 3).fieldOf("ingredients").forGetter(r -> r.recipeItems),
                ItemStack.CODEC.fieldOf("output").forGetter(r -> r.output)
        ).apply(instance, ComputerRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, List<Ingredient>> INGREDIENT_LIST_STREAM_CODEC = ByteBufCodecs.collection(ArrayList::new, Ingredient.CONTENTS_STREAM_CODEC, 3);
        public static final StreamCodec<RegistryFriendlyByteBuf, ComputerRecipe> STREAM_CODEC = StreamCodec.of((buf, recipe) -> {
            INGREDIENT_LIST_STREAM_CODEC.encode(buf, recipe.recipeItems);
            ItemStack.STREAM_CODEC.encode(buf, recipe.output);
        }, buf -> new ComputerRecipe(INGREDIENT_LIST_STREAM_CODEC.decode(buf), ItemStack.STREAM_CODEC.decode(buf)));

        @Override
        public MapCodec<ComputerRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ComputerRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
