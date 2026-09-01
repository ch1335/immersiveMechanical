package com.chen1335.immersiveMechanical.API.energy;

import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.function.Supplier;

public class WrappedEnergy<Wrapped extends IEnergyStorage> implements IEnergyStorage {
    protected final Supplier<Wrapped> supplier;

    public WrappedEnergy(Supplier<Wrapped> supplier) {
        this.supplier = supplier;
    }

    @Override
    public int receiveEnergy(int toReceive, boolean simulate) {
        if (canReceive()) {
            return supplier.get().receiveEnergy(toReceive, simulate);
        }
        return 0;
    }

    @Override
    public int extractEnergy(int toExtract, boolean simulate) {
        if (canExtract()) {
            return supplier.get().extractEnergy(toExtract, simulate);
        }
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return supplier.get().getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return supplier.get().getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return supplier.get().canExtract();
    }

    @Override
    public boolean canReceive() {
        return supplier.get().canReceive();
    }
}
