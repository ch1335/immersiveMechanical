package com.chen1335.immersiveMechanical.mixins.immersive_mechanical;

import com.chen1335.immersiveMechanical.mixinsAPI.ILevelDependentDataExtension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "blusunrize/immersiveengineering/common/blocks/multiblocks/process/MultiblockProcess$LevelDependentData")
public class LevelDependentDataMixin implements ILevelDependentDataExtension {

    @Mutable
    @Shadow
    @Final
    private int maxTicks;

    @Mutable
    @Shadow
    @Final
    private int energyPerTick;

    public void IM$setMaxTicks(int maxTicks) {
        this.maxTicks = maxTicks;
    }

    public void IM$setEnergyPerTick(int energyPerTick) {
        this.energyPerTick = energyPerTick;
    }
}
