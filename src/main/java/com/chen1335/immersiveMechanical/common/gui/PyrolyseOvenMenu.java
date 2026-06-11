package com.chen1335.immersiveMechanical.common.gui;

import blusunrize.immersiveengineering.api.energy.MutableEnergyStorage;
import blusunrize.immersiveengineering.common.gui.IEContainerMenu;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.gui.sync.GenericContainerData;
import blusunrize.immersiveengineering.common.gui.sync.GenericDataSerializers;
import blusunrize.immersiveengineering.common.gui.sync.GetterAndSetter;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.pyrolyseOven.PyrolyseOvenLogic;
import com.chen1335.immersiveMechanical.recipe.PyrolyseOvenRecipe;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class PyrolyseOvenMenu extends IEContainerMenu {

    public GetterAndSetter<Float> process;

    public FluidTank tank;

    public MutableEnergyStorage energyStorage;

    protected PyrolyseOvenMenu(MenuContext ctx, Inventory inventory, ItemStackHandler inv, FluidTank tank, MutableEnergyStorage energyStorage, GetterAndSetter<Float> process) {
        super(ctx);
        this.tank = tank;
        this.energyStorage = energyStorage;
        this.process = process;
        Level level = inventory.player.level();
        for (int i = 0; i < 4; i++)
            this.addSlot(new SlotItemHandler(inv, i, 10 + i % 2 * 18, 24 + i / 2 * 18) {
                @Override
                public boolean mayPlace(@NotNull ItemStack itemStack) {
                    return PyrolyseOvenRecipe.findRecipe(level, itemStack,null) != null;
                }
            });

        for (int i = 0; i < 4; i++)
            this.addSlot(new IESlot.NewOutput(inv, 4 + i, 66 + i % 2 * 18, 24 + i / 2 * 18));

        this.addSlot(new IESlot.NewFluidContainer(inv, 8, 131, 12, IESlot.NewFluidContainer.Filter.ANY));
        this.addSlot(new IESlot.NewOutput(inv, 9, 131, 51));


        ownSlotCount = 10;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++)
                addSlot(new Slot(inventory, j + i * 9 + 9, 7 + j * 18, 85 + 9 + i * 18));
        for (int i = 0; i < 9; i++)
            addSlot(new Slot(inventory, i, 7 + i * 18, 143 + 9));

        this.addGenericData(new GenericContainerData<>(GenericDataSerializers.FLOAT, process));
        this.addGenericData(GenericContainerData.fluid(tank));
        this.addGenericData(GenericContainerData.energy(energyStorage));
    }

    public static PyrolyseOvenMenu makeServer(MenuType<PyrolyseOvenMenu> type, int i, Inventory inventory, MultiblockMenuContext<PyrolyseOvenLogic.State> context) {
        PyrolyseOvenLogic.State state = context.mbContext().getState();
        return new PyrolyseOvenMenu(clientCtx(type, i), inventory, state.inventory, state.tank, state.energyStorage, GetterAndSetter.getterOnly(() -> ((float)state.process / state.processMax)));
    }

    public static PyrolyseOvenMenu makeClient(MenuType<PyrolyseOvenMenu> type, int i, Inventory inventory) {
        return new PyrolyseOvenMenu(clientCtx(type, i), inventory, new ItemStackHandler(10), new FluidTank(PyrolyseOvenLogic.TANK_CAPACITY), new MutableEnergyStorage(64000), GetterAndSetter.standalone(0F));
    }


}
