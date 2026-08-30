package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel;

import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockBE;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockPartBlock;
import blusunrize.immersiveengineering.common.blocks.multiblocks.blockimpl.MultiblockBEHelperCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FlyWheelPartBlockBasic<State extends FlyWheelPart> extends MultiblockPartBlock<State> {
    public FlyWheelPartBlockBasic(Properties properties, MultiblockRegistration<State> multiblock) {
        super(properties, multiblock);
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity selfBe = level.getBlockEntity(pos);
        if (selfBe instanceof IMultiblockBE<?> multiblockBE) {
            MultiblockBEHelperCommon<?> helper = (MultiblockBEHelperCommon<?>) multiblockBE.getHelper();
            IMultiblockState state1 = helper.getState();
            if (state1 instanceof FlyWheelPart flyWheelPart) {
                if (!flyWheelPart.beingDisassembled) {
                    flyWheelPart.beingDisassembled = true;
                    flyWheelPart.linkedParts.forEach(blockPos -> {
                        BlockEntity blockEntity = level.getBlockEntity(blockPos);
                        if (blockEntity instanceof IMultiblockBE<?> be) {
                            IMultiblockContext<?> context1 = be.getHelper().getContext();
                            if (context1 != null && context1.getState() instanceof FlyWheelPart flyWheelPart1) {
                                if (flyWheelPart1 != state1) {
                                    flyWheelPart1.beingDisassembled = true;
                                    be.getHelper().getMultiblock().disassemble().disassemble(
                                            context1.getLevel().getRawLevel(),
                                            context1.getLevel().getAbsoluteOrigin(),
                                            context1.getLevel().getOrientation());
                                }
                            }
                        }
                    });
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
