package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic;

import blusunrize.immersiveengineering.api.energy.MutableEnergyStorage;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IServerTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockLogic;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.CapabilityPosition;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.MultiblockFace;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.RelativeBlockFace;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.ShapeType;
import com.chen1335.immersiveMechanical.common.blockEntities.LargeBatteryCoreTile;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.shapes.LargeBatteryShape;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class LargeBatteryLogic implements IMultiblockLogic<LargeBatteryLogic.State>, IServerTickableComponent<LargeBatteryLogic.State> {
    private static final BlockPos CORE = new BlockPos(1, 2, 1);
    private static final Set<CapabilityPosition> ENERGY_INPUTS = Set.of(new CapabilityPosition(0, 4, 1, RelativeBlockFace.UP), new CapabilityPosition(0, 4, 1, RelativeBlockFace.RIGHT));

    @Override
    public void tickServer(IMultiblockContext<State> iMultiblockContext) {
        if (!iMultiblockContext.getState().checked) {
            if (iMultiblockContext.getLevel().getBlockEntity(CORE) instanceof LargeBatteryCoreTile tile) {
                iMultiblockContext.getState().energy.innerEnergy = tile.energy;
            }
            iMultiblockContext.getState().checked = true;
        }

        for (Supplier<IEnergyStorage> iEnergyStorageSupplier : iMultiblockContext.getState().energyOutputs) {
            IEnergyStorage iEnergyStorage = iEnergyStorageSupplier.get();
            if (iEnergyStorage != null) {
                int received = iEnergyStorage.receiveEnergy(128000000, false);
                iMultiblockContext.getState().energy.extractEnergy(received, false);
            }
        }
    }

    @Override
    public State createInitialState(IInitialMultiblockContext<State> iInitialMultiblockContext) {

        return new State(iInitialMultiblockContext);
    }

    @Override
    public Function<BlockPos, VoxelShape> shapeGetter(ShapeType shapeType) {
        return new LargeBatteryShape();
    }

    @Override
    public void registerCapabilities(CapabilityRegistrar<State> register) {
        register.register(Capabilities.EnergyStorage.BLOCK, (state, position) -> {
            return position.side() != null && !ENERGY_INPUTS.contains(position) ? null : state.energy;
        });
    }


    public static class State implements IMultiblockState {
        public final Set<Supplier<IEnergyStorage>> energyOutputs;

        public static final MultiblockFace ENERGY_OUTS_UP = new MultiblockFace(2, 5, 1, RelativeBlockFace.DOWN);
        public static final MultiblockFace ENERGY_OUTS_RIGHT = new MultiblockFace(3, 4, 1, RelativeBlockFace.RIGHT);

        public State(IInitialMultiblockContext<State> ctx) {
            this.energyOutputs = Set.of(
                    ctx.getCapabilityAt(Capabilities.EnergyStorage.BLOCK, ENERGY_OUTS_UP),
                    ctx.getCapabilityAt(Capabilities.EnergyStorage.BLOCK, ENERGY_OUTS_RIGHT)
            );


        }

        public boolean checked = false;

        public EnergyStorageHolder energy = new EnergyStorageHolder();

        @Override
        public void writeSaveNBT(CompoundTag compoundTag, HolderLookup.Provider provider) {

        }

        @Override
        public void readSaveNBT(CompoundTag compoundTag, HolderLookup.Provider provider) {

        }
    }

    public static class EnergyStorageHolder implements IEnergyStorage {
        public MutableEnergyStorage innerEnergy = new MutableEnergyStorage(0);

        @Override
        public int receiveEnergy(int toReceive, boolean simulate) {
            return innerEnergy.receiveEnergy(toReceive, simulate);
        }

        @Override
        public int extractEnergy(int toExtract, boolean simulate) {
            return innerEnergy.extractEnergy(toExtract, simulate);
        }

        @Override
        public int getEnergyStored() {
            return innerEnergy.getEnergyStored();
        }

        @Override
        public int getMaxEnergyStored() {
            return innerEnergy.getMaxEnergyStored();
        }

        @Override
        public boolean canExtract() {
            return innerEnergy.canExtract();
        }

        @Override
        public boolean canReceive() {
            return innerEnergy.canReceive();
        }
    }
}
