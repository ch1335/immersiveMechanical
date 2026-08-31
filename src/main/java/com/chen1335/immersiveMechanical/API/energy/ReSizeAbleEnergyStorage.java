package com.chen1335.immersiveMechanical.API.energy;

import blusunrize.immersiveengineering.api.energy.MutableEnergyStorage;

public class ReSizeAbleEnergyStorage extends MutableEnergyStorage implements IReSizeAbleEnergyStorage {
    public ReSizeAbleEnergyStorage(int capacity, int maxInsert, int maxExtract) {
        super(capacity, maxInsert, maxExtract);
    }

    @Override
    public void setMaxEnergyStored(int max) {
        capacity = max;
    }
}
