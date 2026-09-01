package com.chen1335.immersiveMechanical.API;

import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public interface IMBEConstructor<State extends IMultiblockState, T extends BlockEntity> {
    T make(BlockEntityType<?> type, BlockPos pos, BlockState state, MultiblockRegistration<State> multiblock);

}
