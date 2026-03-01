package com.chen1335.immersiveMechanical.mixins.immersive_mechanical;

import blusunrize.immersiveengineering.common.blocks.metal.EnergyConnectorBlockEntity;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnergyConnectorBlockEntity.class)
public interface EnergyConnectorBlockEntityAccessor {
    @Accessor("voltage")
    String getVoltage();

    @Accessor("LENGTH")
    static Object2FloatMap<Pair<String, Boolean>> IM$getLength() {
        throw new RuntimeException();
    }
}
