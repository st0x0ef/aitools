package com.st0x0ef.aitools.common.compats.jei;

import com.st0x0ef.aitools.AITools;
import com.st0x0ef.aitools.common.registries.ItemsRegistry;
import com.st0x0ef.aitools.common.registries.RecipesRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

@JeiPlugin
public class JeiCompat implements IModPlugin {
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
        ClientLevel level = Minecraft.getInstance().level;

        if (level != null) {
            registry.addRecipes(ComputerCategory.RECIPE, level.getRecipeManager().getAllRecipesFor(RecipesRegistry.COMPUTER_TYPE.get()).stream().map(RecipeHolder::value).toList());
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(ItemsRegistry.COMPUTER_ITEM.get().getDefaultInstance(), ComputerCategory.RECIPE);
    }
}
