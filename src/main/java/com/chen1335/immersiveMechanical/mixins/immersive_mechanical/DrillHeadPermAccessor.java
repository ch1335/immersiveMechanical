package com.chen1335.immersiveMechanical.mixins.immersive_mechanical;

import blusunrize.immersiveengineering.common.items.DrillheadItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DrillheadItem.DrillHeadPerm.class)
public interface DrillHeadPermAccessor {
    @Accessor("drillSize")
    int IM$getDrillSize();
}
