package com.chen1335.immersiveMechanical.common.blocks.multiblocks.PartBlocks;

import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockPartBlock;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.GreenHouseLogic;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

public class GreenHousePartBlock extends MultiblockPartBlock<GreenHouseLogic.State> {
    public GreenHousePartBlock(MultiblockRegistration<GreenHouseLogic.State> multiblock) {
        super(IEBlocks.METAL_PROPERTIES_NO_OCCLUSION.get().lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 15 : 0), multiblock);
        registerDefaultState(this.defaultBlockState().setValue(BlockStateProperties.LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.LIT);
        super.createBlockStateDefinition(builder);
    }
}
