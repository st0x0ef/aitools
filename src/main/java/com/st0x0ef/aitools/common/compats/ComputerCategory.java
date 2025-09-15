package com.st0x0ef.aitools.common.compats;

import com.st0x0ef.aitools.AITools;
import com.st0x0ef.aitools.common.recipes.ComputerRecipe;
import com.st0x0ef.aitools.common.registries.ItemsRegistry;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record ComputerCategory(IGuiHelper guiHelper) implements IRecipeCategory<ComputerRecipe> {
    public static final RecipeType<ComputerRecipe> RECIPE = new RecipeType<>(ResourceLocation.parse("computer"), ComputerRecipe.class);
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(AITools.MODID, "textures/gui/computer_jei.png");

    @Override
    public RecipeType<ComputerRecipe> getRecipeType() {
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
        if (Minecraft.getInstance().level == null) return;

        builder.addSlot(RecipeIngredientRole.INPUT, 26, 24).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 57, 24).addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 88, 24).addIngredients(recipe.getIngredients().get(2));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 135, 24).addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
    }
}
