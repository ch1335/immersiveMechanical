package com.chen1335.immersiveMechanical.mixins.immersive_mechanical;

import blusunrize.immersiveengineering.common.items.upgrades.ToolUpgrade;
import blusunrize.immersiveengineering.common.items.upgrades.ToolUpgradeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ToolUpgradeItem.class)
public interface ToolUpgradeItemAccessor {
    @Accessor("type")
    ToolUpgrade IM$getUpgradeType();
}
