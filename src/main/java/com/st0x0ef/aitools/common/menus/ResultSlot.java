package com.st0x0ef.aitools.common.menus;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ResultSlot extends Slot {

    public ResultSlot(Container container, int i, int x, int y) {
        super(container, i, x, y);
    }

    public boolean mayPlace(ItemStack itemStack) {
        return false;
    }
}
