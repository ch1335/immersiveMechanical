package com.chen1335.immersiveMechanical.mixins;

import blusunrize.immersiveengineering.common.blocks.metal.EnergyConnectorBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnergyConnectorBlockEntity.class)
public interface EnergyConnectorBlockEntityAccessor {
    @Accessor("voltage")
    String getVoltage();
}
