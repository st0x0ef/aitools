package com.st0x0ef.aitools.client.screens;

import com.st0x0ef.aitools.AITools;
import com.st0x0ef.aitools.common.menus.ComputerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ComputerScreen extends AbstractContainerScreen<ComputerMenu> {

    public static final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(AITools.MODID, "textures/gui/computer.png");

    public ComputerScreen(ComputerMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);

        this.imageWidth = 176;
        this.imageHeight = 166;

        this.titleLabelY = 6;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 16777215, false);
        guiGraphics.drawString(this.font, Component.literal("Running DiamondOS..."), this.titleLabelX, this.titleLabelY + 18, 16777215, false);
    }

}
