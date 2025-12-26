package com.chen1335.immersiveMechanical.common.gui;

import blusunrize.immersiveengineering.api.crafting.ClocheFertilizer;
import blusunrize.immersiveengineering.api.crafting.ClocheRecipe;
import blusunrize.immersiveengineering.api.energy.MutableEnergyStorage;
import blusunrize.immersiveengineering.common.gui.IEContainerMenu;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.gui.sync.GenericContainerData;
import blusunrize.immersiveengineering.common.gui.sync.GenericDataSerializers;
import blusunrize.immersiveengineering.common.gui.sync.GetterAndSetter;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.GreenHouseLogic;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class GreenHouseMenu extends IEContainerMenu {
    public final MutableEnergyStorage energyStorage;
    public final FluidTank tank;
    public final GetterAndSetter<Integer> fertilizerAmount;
    public final GetterAndSetter<Float> fertilizerMod;
    public final IntObjectMap<GetterAndSetter<Float>> processInfo;


    public static GreenHouseMenu makeServer(MenuType<GreenHouseMenu> type, int id, Inventory inventory, MultiblockMenuContext<GreenHouseLogic.State> ctx) {
        GreenHouseLogic.State state = ctx.mbContext().getState();

        GetterAndSetter<Integer> fertilizerAmount = GetterAndSetter.getterOnly(() -> state.fertilizerAmount);

        GetterAndSetter<Float> fertilizerMod = GetterAndSetter.getterOnly(() -> state.fertilizerMod);
        IntObjectMap<GetterAndSetter<Float>> processInfo = new IntObjectHashMap<>(16);
        state.processUnits.forEach((integer, processUnit) -> {
            processInfo.put(integer, GetterAndSetter.getterOnly(() -> {
                if (processUnit.tickRequire == 0) {
                    return 0F;
                }
                return processUnit.growth / (float) processUnit.tickRequire;
            }));
        });


        return new GreenHouseMenu(
                multiblockCtx(type, id, ctx),
                inventory,
                state.energyStorage,
                state.tank,
                state.seeds,
                state.soils,
                state.fertilizer,
                state.products,
                fertilizerAmount,
                fertilizerMod,
                processInfo
        );
    }

    public static GreenHouseMenu makeClient(MenuType<GreenHouseMenu> type, int id, Inventory inventory) {
        IntObjectMap<GetterAndSetter<Float>> processInfo = new IntObjectHashMap<>(16);
        for (int i = 0; i < 16; i++) {
            processInfo.put(i, GetterAndSetter.standalone(0F));
        }
        return new GreenHouseMenu(
                clientCtx(type, id),
                inventory,
                new MutableEnergyStorage(64000),
                new FluidTank(4000),
                new ItemStackHandler(16),
                new ItemStackHandler(4),
                new ItemStackHandler(),
                new ItemStackHandler(16),
                GetterAndSetter.standalone(0),
                GetterAndSetter.standalone(0F),
                processInfo
        );
    }

    protected GreenHouseMenu(MenuContext ctx,
                             Inventory playerInventory,
                             MutableEnergyStorage energyStorage,
                             FluidTank tank,
                             ItemStackHandler seeds,
                             ItemStackHandler soils,
                             ItemStackHandler fertilizer,
                             ItemStackHandler products,
                             GetterAndSetter<Integer> fertilizerAmount,
                             GetterAndSetter<Float> fertilizerMod,
                             IntObjectMap<GetterAndSetter<Float>> processInfo
    ) {
        super(ctx);
        this.fertilizerAmount = fertilizerAmount;
        this.fertilizerMod = fertilizerMod;
        this.energyStorage = energyStorage;
        this.tank = tank;
        this.processInfo = processInfo;
        Level level = playerInventory.player.level();
        this.addSlot(new GreenHouseSlot(IESlot.Cloche.Type.FERTILIZER, fertilizer, 0, 9, 74, level));

        this.addSlot(new GreenHouseSlot(IESlot.Cloche.Type.SOIL, soils, 0, 43, 90, level));
        this.addSlot(new GreenHouseSlot(IESlot.Cloche.Type.SOIL, soils, 1, 64, 90, level));
        this.addSlot(new GreenHouseSlot(IESlot.Cloche.Type.SOIL, soils, 2, 85, 90, level));
        this.addSlot(new GreenHouseSlot(IESlot.Cloche.Type.SOIL, soils, 3, 106, 90, level));

        for (int i = 0; i < seeds.getSlots(); i++) {
            this.addSlot(new GreenHouseSlot(IESlot.Cloche.Type.SEED, seeds, i, 43 + i / 4 * 21, 10 + 18 * (i % 4), level));
        }

        for (int i = 0; i < products.getSlots(); i++) {
            this.addSlot(new IESlot.NewOutput(products, i, 142 + i / 4 * 18, 10 + 18 * (i % 4)));
        }

        int i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 39 + j * 18, 121 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 39 + i * 18, 179));
        }

        this.ownSlotCount = 37;
        this.addGenericData(GenericContainerData.fluid(this.tank));
        this.addGenericData(GenericContainerData.energy(this.energyStorage));
        this.addGenericData(new GenericContainerData<>(GenericDataSerializers.INT32, fertilizerAmount));
        this.addGenericData(new GenericContainerData<>(GenericDataSerializers.FLOAT, fertilizerMod));

        for (int j = 0; j < seeds.getSlots(); j++) {
            this.addGenericData(new GenericContainerData<>(GenericDataSerializers.FLOAT, processInfo.get(j)));
        }

    }

    public static class GreenHouseSlot extends IESlot.Cloche {

        private final Level GreenHouseSlot_level;

        private final Type GreenHouseSlot_type;

        public GreenHouseSlot(Type type, IItemHandler inv, int id, int x, int y, Level level) {
            super(type, inv, id, x, y, level);
            GreenHouseSlot_level = level;
            GreenHouseSlot_type = type;
        }

        public boolean mayPlace(ItemStack itemStack) {
            if (itemStack.isEmpty()) {
                return false;
            } else if (GreenHouseSlot_type == IESlot.Cloche.Type.FERTILIZER) {
                return ClocheFertilizer.isValidFertilizer(GreenHouseSlot_level, itemStack);
            } else {
                IItemHandler inv = this.getItemHandler();
                if (GreenHouseSlot_type == IESlot.Cloche.Type.SOIL) {
                    return ClocheRecipe.isValidCombinationInMenu(inv.getStackInSlot(getSlotIndex()), itemStack, GreenHouseSlot_level);
                } else {
                    return GreenHouseSlot_type != Type.SEED || ClocheRecipe.isValidCombinationInMenu(itemStack, inv.getStackInSlot(getSlotIndex()), GreenHouseSlot_level);
                }
            }
        }
    }
}
