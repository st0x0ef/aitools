package com.st0x0ef.aitools.common.registries;

import com.st0x0ef.aitools.AITools;
import com.st0x0ef.aitools.common.recipes.ComputerRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RecipesRegistry {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE = DeferredRegister.create(Registries.RECIPE_TYPE, AITools.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, AITools.MODID);


    public static final DeferredHolder<RecipeType<?>, Type<ComputerRecipe>> COMPUTER_TYPE = RECIPE_TYPE.register("computer", () -> new Type<>("computer"));
    public static final DeferredHolder<RecipeSerializer<?>, ComputerRecipe.Serializer> COMPUTER_SERIALIZED = RECIPE_SERIALIZER.register("computer", ComputerRecipe.Serializer::new);


    public record Type<T extends Recipe<?>>(String id) implements RecipeType<T> {
        @Override
        public String toString() {
            return AITools.MODID + ":" + id;
        }
    }
}
