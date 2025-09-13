package com.st0x0ef.aitools.common.menus;

import com.st0x0ef.aitools.common.registries.MenuTypesRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ComputerMenu extends AbstractContainerMenu {

    private final Container inventory;

    public ComputerMenu(int syncId, Inventory inventory) {
        super(MenuTypesRegistry.COMPUTER.get(), syncId);

        Container container = new SimpleContainer(4);

        checkContainerSize(container, 4);
        this.inventory = container;

        addSlots(inventory);

        addPlayerHotbar(inventory);
        addPlayerInventory(inventory);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }
            else {
                slot.setChanged();
            }
        }

        return newStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }


    private void addSlots(Container inventory) {
        this.addSlot(new Slot(inventory, 0, 26, 44));
        this.addSlot(new Slot(inventory, 1, 57, 44));
        this.addSlot(new Slot(inventory, 2, 88, 44));

        this.addSlot(new Slot(inventory, 3, 135, 44));

    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, 4 + l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, 4 + i, 8 + i * 18, 142));
        }
    }
}
