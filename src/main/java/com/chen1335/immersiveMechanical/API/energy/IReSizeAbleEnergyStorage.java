package com.chen1335.immersiveMechanical.API.energy;

import net.neoforged.neoforge.energy.IEnergyStorage;

public interface IReSizeAbleEnergyStorage extends IEnergyStorage {

    void setMaxEnergyStored(int max);
}
