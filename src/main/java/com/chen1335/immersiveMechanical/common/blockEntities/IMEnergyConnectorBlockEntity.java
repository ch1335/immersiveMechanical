package com.chen1335.immersiveMechanical.common.blockEntities;

import blusunrize.immersiveengineering.common.blocks.metal.EnergyConnectorBlockEntity;
import com.chen1335.immersiveMechanical.mixins.immersive_mechanical.EnergyConnectorBlockEntityAccessor;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class IMEnergyConnectorBlockEntity extends EnergyConnectorBlockEntity {

    public IMEnergyConnectorBlockEntity(String voltage, boolean relay, BlockPos pos, BlockState state) {
        super(voltage, relay, pos, state);
    }

}
