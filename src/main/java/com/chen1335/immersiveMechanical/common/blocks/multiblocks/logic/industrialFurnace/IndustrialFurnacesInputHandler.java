package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.industrialFurnace;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class IndustrialFurnacesInputHandler implements IItemHandler {
    private final ItemStackHandler inventory;
    private final Runnable onChanged;

    public IndustrialFurnacesInputHandler(ItemStackHandler inventory, Runnable onChanged) {
        this.inventory = inventory;
        this.onChanged = onChanged;
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty())
            return stack;
        stack = stack.copy();
        List<Integer> possibleSlots = new ArrayList<>(IndustrialFurnacesLogic.IN_SLOT_COUNT);
        for (int i = IndustrialFurnacesLogic.FIRST_IN_SLOT; i < IndustrialFurnacesLogic.IN_SLOT_COUNT; i++) {
            ItemStack here = inventory.getStackInSlot(i);
            if (here.isEmpty()) {
                if (!simulate)
                    inventory.setStackInSlot(i, stack);
                onChanged.run();
                return ItemStack.EMPTY;
            } else if (ItemStack.isSameItemSameComponents(stack, here) && here.getCount() < here.getMaxStackSize())
                possibleSlots.add(i);
        }
        possibleSlots.sort(Comparator.comparingInt(a -> inventory.getStackInSlot(a).getCount()));
        for (int i : possibleSlots) {
            ItemStack here = inventory.getStackInSlot(i);
            int fillCount = Math.min(here.getMaxStackSize() - here.getCount(), stack.getCount());
            if (!simulate)
                here.grow(fillCount);
            stack.shrink(fillCount);
            if (stack.isEmpty()) {
                onChanged.run();
                return ItemStack.EMPTY;
            }
        }
        onChanged.run();
        return stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return inventory.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return inventory.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return inventory.isItemValid(slot, stack);
    }
}
