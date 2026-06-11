package com.chen1335.immersiveMechanical.mixinsAPI;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;

public interface IMultiblockProcessExtension<R extends MultiblockRecipe> {

    default int getMaxTicks(int old) {
        return old;
    }

    default int getEnergyPerTick(int old) {
        return old;
    }
}
