package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel;

import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockBEHelper;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockBEHelperMaster;
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
    public void onRemove(BlockState blockState, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity selfBe = level.getBlockEntity(pos);
        if (selfBe instanceof IMultiblockBE<?> multiblockBE) {
            IMultiblockState multiblockState = multiblockBE.getHelper().getState();
            if (multiblockState instanceof FlyWheelPart flyWheelPart) {
                if (!flyWheelPart.beingDisassembled) {
                    flyWheelPart.beingDisassembled = true;
                    flyWheelPart.linkedParts.forEach(blockPos -> {
                        BlockEntity blockEntity = level.getBlockEntity(blockPos);
                        if (blockEntity instanceof IMultiblockBE<?> be) {
                            IMultiblockBEHelperMaster<?> masterHelper = ((MultiblockBEHelperCommon<?>) be.getHelper()).getMasterHelperDuringDisassembly();
                            if (masterHelper != null) {
                                IMultiblockContext<?> context = masterHelper.getContext();
                                if (context.getState() instanceof FlyWheelPart flyWheelPart1) {
                                    if (flyWheelPart1 != multiblockState) {
                                        flyWheelPart1.beingDisassembled = true;
                                        be.getHelper().getMultiblock().disassemble().disassemble(
                                                context.getLevel().getRawLevel(),
                                                context.getLevel().getAbsoluteOrigin(),
                                                context.getLevel().getOrientation());
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }
        super.onRemove(blockState, level, pos, newState, isMoving);
    }
}
