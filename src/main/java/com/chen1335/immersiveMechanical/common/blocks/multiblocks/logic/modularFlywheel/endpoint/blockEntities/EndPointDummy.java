package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.endpoint.blockEntities;

import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockBE;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockBlockEntityDummy;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.endpoint.EndPointLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class EndPointDummy extends MultiblockBlockEntityDummy<EndPointLogic.State> {
    public EndPointDummy(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState, MultiblockRegistration<EndPointLogic.State> multiblock) {
        super(type, worldPosition, blockState, multiblock);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null && level.getBlockEntity(getBlockPos()) instanceof IMultiblockBE<?> be && be.getHelper().getState() instanceof EndPointLogic.State state) {
            state.dummyBEs.add(this);
        }
    }
}
