package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.flywheel;

import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlyWheelPart;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlyWheelPartBlockBasic;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FlyWheelPartBlock<State extends FlyWheelPart> extends FlyWheelPartBlockBasic<State> {
    public FlyWheelPartBlock(Properties properties, MultiblockRegistration<State> multiblock) {
        super(properties, multiblock);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
