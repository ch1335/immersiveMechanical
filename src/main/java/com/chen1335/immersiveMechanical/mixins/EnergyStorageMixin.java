package com.chen1335.immersiveMechanical.mixins;

import com.chen1335.immersiveMechanical.mixinsAPI.IEnergyStorageMixin;
import net.neoforged.neoforge.energy.EnergyStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EnergyStorage.class)
public class EnergyStorageMixin implements IEnergyStorageMixin {
    @Shadow
    protected int capacity;

    @Unique
    public void im$setNewCap(int cap) {
        capacity = cap;
    }
}
