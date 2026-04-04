package com.chen1335.immersiveMechanical.common;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public class IMItemHandlerHelper {
    public static ItemStack insertItemStacked(IItemHandler inventory, ItemStack stack, int from, int to, boolean simulate) {
        if (inventory == null || stack.isEmpty())
            return stack;

        if (!stack.isStackable()) {
            return ItemHandlerHelper.insertItem(inventory, stack, simulate);
        }

        int sizeInventory = Math.min(inventory.getSlots(), to + 1);

        for (int i = from; i < sizeInventory; i++) {
            ItemStack slot = inventory.getStackInSlot(i);
            if (ItemStack.isSameItemSameComponents(slot, stack)) {
                stack = inventory.insertItem(i, stack, simulate);

                if (stack.isEmpty()) {
                    break;
                }
            }
        }

        if (!stack.isEmpty()) {
            for (int i = from; i < sizeInventory; i++) {
                if (inventory.getStackInSlot(i).isEmpty()) {
                    stack = inventory.insertItem(i, stack, simulate);
                    if (stack.isEmpty()) {
                        break;
                    }
                }
            }
        }

        return stack;
    }
}
