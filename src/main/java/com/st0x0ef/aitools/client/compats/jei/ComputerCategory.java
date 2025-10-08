/*package com.st0x0ef.aitools.client.compats.jei;

import com.st0x0ef.aitools.AITools;
import com.st0x0ef.aitools.common.recipes.ComputerRecipe;
import com.st0x0ef.aitools.common.registries.ItemsRegistry;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record ComputerCategory(IGuiHelper guiHelper) implements IRecipeCategory<ComputerRecipe> {
    public static final IRecipeType<ComputerRecipe> RECIPE = IRecipeType.create(ResourceLocation.fromNamespaceAndPath(AITools.MODID, "computer"), ComputerRecipe.class);
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(AITools.MODID, "textures/gui/computer_jei.png");

    @Override
    public IRecipeType<ComputerRecipe> getRecipeType() {
        return RECIPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.aitools.computer");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return guiHelper.createDrawableItemStack(new ItemStack(ItemsRegistry.COMPUTER_ITEM.get()));
    }

    @Override
    public void draw(ComputerRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(TEXTURE, 0, 0, 0, 0, 176, 65, 176, 65);
    }

    @Override
    public int getWidth() {
        return 176;
    }

    @Override
    public int getHeight() {
        return 65;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ComputerRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 24).add(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 57, 24).add(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 88, 24).add(recipe.getIngredients().get(2));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 135, 24).add(recipe.output());
    }
}
*/