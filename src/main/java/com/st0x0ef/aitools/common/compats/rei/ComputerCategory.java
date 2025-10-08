/*package com.st0x0ef.aitools.common.compats.rei;

import com.st0x0ef.aitools.AITools;
import com.st0x0ef.aitools.common.registries.ItemsRegistry;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedList;
import java.util.List;

public class ComputerCategory implements DisplayCategory<BasicDisplay> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(AITools.MODID, "textures/gui/computer_jei.png");
    public static final CategoryIdentifier<ComputerDisplay> COMPUTER_CATEGORY = CategoryIdentifier.of(AITools.MODID, "computer");


    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return COMPUTER_CATEGORY;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.aitools.computer");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ItemsRegistry.COMPUTER_ITEM.get());
    }

    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        final Point startPoint = new Point(bounds.getX(), bounds.getY());
        List<Widget> widgets = new LinkedList<>();

        widgets.add(Widgets.createTexturedWidget(TEXTURE, bounds, 0, 0, 176, 65));

        inputSlotAdder(widgets, 0, 26, 24, startPoint, display);
        inputSlotAdder(widgets, 1, 57, 24, startPoint, display);
        inputSlotAdder(widgets, 2, 88, 24, startPoint, display);

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 135, startPoint.y + 24)).entries(display.getOutputEntries().getFirst()).markOutput().disableBackground());

        return widgets;
    }

    private static void inputSlotAdder(List<Widget> widgets, int slotIndex, int x, int y, Point startPoint, BasicDisplay display) {
        widgets.add(Widgets.createSlot(new Point(startPoint.x + x, startPoint.y + y)).entries(display.getInputEntries().get(slotIndex)).markInput().disableBackground());
    }

    @Override
    public int getDisplayWidth(BasicDisplay display) {
        return 176;
    }

    @Override
    public int getDisplayHeight() {
        return 65;
    }
}
*/