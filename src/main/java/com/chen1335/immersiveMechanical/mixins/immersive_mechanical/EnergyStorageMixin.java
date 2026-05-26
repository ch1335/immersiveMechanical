package com.chen1335.immersiveMechanical.mixins.immersive_mechanical;

import com.chen1335.immersiveMechanical.mixinsAPI.IEnergyStorageExtension;
import net.neoforged.neoforge.energy.EnergyStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EnergyStorage.class)
public class EnergyStorageMixin implements IEnergyStorageExtension {
    @Shadow
    protected int capacity;

    @Unique
    public void IM$setNewCap(int cap) {
        capacity = cap;
    }
}
