package com.st0x0ef.aitools.common.compats.rei;

import com.st0x0ef.aitools.common.recipes.ComputerRecipe;
import com.st0x0ef.aitools.common.registries.BlocksRegistry;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.world.item.crafting.RecipeType;

@REIPluginClient
public class ReiCompat implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new ComputerCategory());

        registry.addWorkstations(ComputerCategory.COMPUTER_CATEGORY, EntryStacks.of(BlocksRegistry.COMPUTER.get()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(ComputerRecipe.class, (RecipeType<? super ComputerRecipe>) ComputerRecipe.TYPE, ComputerDisplay::new);
    }
}
