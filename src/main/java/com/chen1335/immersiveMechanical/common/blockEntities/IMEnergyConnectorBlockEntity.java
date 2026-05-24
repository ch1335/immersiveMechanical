package com.chen1335.immersiveMechanical.common.blockEntities;

import blusunrize.immersiveengineering.common.blocks.metal.EnergyConnectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class IMEnergyConnectorBlockEntity extends EnergyConnectorBlockEntity {
    public IMEnergyConnectorBlockEntity(BlockEntityType<? extends IMEnergyConnectorBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
