package com.chen1335.immersiveMechanical.common.gui;

import blusunrize.immersiveengineering.api.energy.IMutableEnergyStorage;
import blusunrize.immersiveengineering.api.energy.MutableEnergyStorage;
import blusunrize.immersiveengineering.api.tool.IDrillHead;
import blusunrize.immersiveengineering.common.gui.IEContainerMenu;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.gui.sync.GenericContainerData;
import blusunrize.immersiveengineering.common.gui.sync.GetterAndSetter;
import blusunrize.immersiveengineering.common.items.upgrades.ToolUpgrade;
import blusunrize.immersiveengineering.common.items.upgrades.ToolUpgradeItem;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.smallMiningMachine.SmallMiningMachineLogic;
import com.chen1335.immersiveMechanical.common.gui.sync.IMGenericDataSerializers;
import com.chen1335.immersiveMechanical.mixins.immersive_mechanical.ToolUpgradeItemAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class SmallMiningMachineMenu extends IEContainerMenu {

    private final SmallMiningMachineLogic.State state;
    public final IMutableEnergyStorage energyStorage;
    public final GetterAndSetter<BlockPos> current;
    public final GetterAndSetter<Boolean> active;

    public static SmallMiningMachineMenu makeServer(MenuType<SmallMiningMachineMenu> smallMiningMachineMenuMenuType, int i, Inventory inventory, MultiblockMenuContext<SmallMiningMachineLogic.State> stateMultiblockMenuContext) {
        SmallMiningMachineLogic.State state = stateMultiblockMenuContext.mbContext().getState();
        return new SmallMiningMachineMenu(
                state,
                multiblockCtx(smallMiningMachineMenuMenuType, i, stateMultiblockMenuContext),
                inventory,
                state.inventory,
                state.getEnergy(),
                GetterAndSetter.getterOnly(() -> {
                    if (state.currentMinedPos == null) {
                        return BlockPos.ZERO;
                    }
                    return new BlockPos(state.currentMinedPos.getX() - state.minX, state.currentMinedPos.getY(), state.currentMinedPos.getZ() - state.minZ);
                }),
                GetterAndSetter.getterOnly(() -> state.active)
        );
    }

    public static SmallMiningMachineMenu makeClient(MenuType<SmallMiningMachineMenu> smallMiningMachineMenuMenuType, int i, Inventory inventory) {
        return new SmallMiningMachineMenu(
                null,
                clientCtx(smallMiningMachineMenuMenuType, i),
                inventory,
                new ItemStackHandler(13),
                new MutableEnergyStorage(SmallMiningMachineLogic.ENERGY_CAPACITY),
                GetterAndSetter.standalone(BlockPos.ZERO),
                GetterAndSetter.standalone(false)
        );
    }

    protected SmallMiningMachineMenu(SmallMiningMachineLogic.State state,
                                     MenuContext ctx,
                                     Inventory playerInventory,
                                     ItemStackHandler itemStackHandler,
                                     IMutableEnergyStorage energyStorage,
                                     GetterAndSetter<BlockPos> current,
                                     GetterAndSetter<Boolean> active
    ) {
        super(ctx);
        this.state = state;
        this.energyStorage = energyStorage;
        this.current = current;
        this.active = active;
        addSlot(new IESlot.WithPredicate(itemStackHandler, 0, 34, 34, (itemStack) -> itemStack.getItem() instanceof IDrillHead));
        addSlot(new Upgrades(itemStackHandler, 1, 10, 10));
        addSlot(new Upgrades(itemStackHandler, 2, 34, 10));
        addSlot(new Upgrades(itemStackHandler, 3, 58, 10));

        for (int i = 0; i < 9; i++)
            this.addSlot(new IESlot.NewOutput(itemStackHandler, 4 + i, 91 + i % 3 * 18, 17 + i / 3 * 18));

        ownSlotCount = 13;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++)
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 85 + i * 18));
        for (int i = 0; i < 9; i++)
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 143));

        addGenericData(GenericContainerData.energy(energyStorage));
        addGenericData(GenericContainerData.bool(active, active));
        addGenericData(new GenericContainerData<>(IMGenericDataSerializers.BLOCK_POS, current));
    }

    @Override
    public void receiveMessageFromScreen(CompoundTag nbt) {
        if (state != null) {
            int buttonId = nbt.getInt("buttonId");
            if (buttonId == 0) {
                state.reset();
            }
        }
    }

    protected static class Upgrades extends SlotItemHandler {

        public Upgrades(IItemHandler iItemHandler, int slot, int x, int y) {
            super(iItemHandler, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {

            for (int i = 0; i < 4; i++) {
                ItemStack itemStack = getItemHandler().getStackInSlot(i);
                if (ItemStack.isSameItem(itemStack, stack) && i != this.index) {
                    return false;
                }
            }


            if (!stack.isEmpty() && stack.getItem() instanceof ToolUpgradeItem toolUpgradeItem) {
                ToolUpgrade toolUpgrade = ((ToolUpgradeItemAccessor) toolUpgradeItem).IM$getUpgradeType();
                return toolUpgrade == ToolUpgrade.DRILL_FORTUNE || toolUpgrade == ToolUpgrade.DRILL_DAMAGE || toolUpgrade == ToolUpgrade.DRILL_LUBE;
            }
            return false;
        }
    }
}
