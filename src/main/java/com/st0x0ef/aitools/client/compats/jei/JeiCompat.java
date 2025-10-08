/*
package com.st0x0ef.aitools.client.compats.jei;

import com.st0x0ef.aitools.AITools;
import com.st0x0ef.aitools.common.recipes.ComputerRecipe;
import com.st0x0ef.aitools.common.registries.ItemsRegistry;
import com.st0x0ef.aitools.common.registries.RecipesRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@JeiPlugin
public class JeiCompat implements IModPlugin {
    private static RecipeMap clientSyncedRecipes = RecipeMap.EMPTY;

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(AITools.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new ComputerCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        registry.addRecipes(ComputerCategory.RECIPE, getComputerRecipes());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addCraftingStation(ComputerCategory.RECIPE, ItemsRegistry.COMPUTER_ITEM.get().getDefaultInstance());
    }

    private static List<ComputerRecipe> getComputerRecipes() {
        List<ComputerRecipe> result = new ArrayList<>();

        Collection<RecipeHolder<ComputerRecipe>> craftingRecipes = clientSyncedRecipes.byType(RecipesRegistry.COMPUTER_TYPE.get());

        for (RecipeHolder<ComputerRecipe> recipe : craftingRecipes) {
            result.add(recipe.value());
        }

        AITools.LOGGER.error("Size : {}", result.size());
        return result;
    }

    public static void setClientSyncedRecipes(RecipeMap recipes) {
        clientSyncedRecipes = recipes;
    }
}
*/