package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.flywheel;

import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import com.chen1335.immersiveMechanical.API.IMPartBlockHelper;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlyWheelPart;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlyWheelPartBlockBasic;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FlyWheelPartBlock<State extends FlyWheelPart> extends FlyWheelPartBlockBasic<State> implements IMPartBlockHelper<State> {
    public FlyWheelPartBlock(Properties properties, MultiblockRegistration<State> multiblock) {
        super(properties, multiblock);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void entityInside(@NotNull BlockState blockState, Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        State state = getState(level, pos);
        if (state != null) {
            entity.hurt(level.damageSources().cactus(), 1);
        }
        super.entityInside(blockState, level, pos, entity);
    }

}
