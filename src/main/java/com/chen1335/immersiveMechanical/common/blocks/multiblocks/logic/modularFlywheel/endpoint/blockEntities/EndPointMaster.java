package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.endpoint.blockEntities;

import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockBlockEntityMaster;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.endpoint.EndPointLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class EndPointMaster extends MultiblockBlockEntityMaster<EndPointLogic.State> {
    public EndPointMaster(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState, MultiblockRegistration<EndPointLogic.State> multiblock) {
        super(type, worldPosition, blockState, multiblock);
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }
}
