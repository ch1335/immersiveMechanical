package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.endpoint;

import blusunrize.immersiveengineering.api.multiblocks.BlockMatcher;
import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockBE;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlyWheelPart;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks.TestAbleTemplateMultiblock;
import com.chen1335.immersiveMechanical.definitions.IMMultiblocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class EndPointTemplate extends TestAbleTemplateMultiblock {

    public EndPointTemplate(ResourceLocation loc, BlockPos masterFromOrigin, BlockPos triggerFromOrigin, BlockPos size, MultiblockRegistration<?> logic, List<BlockMatcher.MatcherPredicate> additionalPredicates, float manualScale) {
        super(loc, masterFromOrigin, triggerFromOrigin, size, logic, additionalPredicates, manualScale);
    }


    @Override
    public boolean createStructure(Level level, BlockPos pos, Direction side, Player player) {
        TestAbleTemplateMultiblock flywheel = (TestAbleTemplateMultiblock) IMMultiblocks.FLYWHEEL.multiblock();
        EndPointTemplate flywheelEndpoint = (EndPointTemplate) IMMultiblocks.FLYWHEEL_ENDPOINT.multiblock();
        boolean success = true;
        List<Runnable> runnables = new ArrayList<>();
        List<BlockPos> linkedParts = new ArrayList<>();
        if (canForm(level, pos, side, player)) {
            runnables.add(() -> {
                this.innerCreateStructure(level, pos, side, player);
                linkedParts.add(getMasterPose(level,pos));
            });
        } else {
            success = false;
        }
        int length = 0;
        while (success) {
            BlockPos relative = pos.relative(side.getOpposite(), length + 3);
            if (flywheel.canForm(level, relative, side, player)) {
                length++;
                BlockPos finalRelative = relative;
                runnables.add(() -> {
                    flywheel.createStructure(level, finalRelative, side, player);
                    linkedParts.add(getMasterPose(level,finalRelative));
                });
            } else {
                relative = pos.relative(side.getOpposite(), length + 5);
                if (flywheelEndpoint.canForm(level, relative, side.getOpposite(), player)) {
                    BlockPos finalRelative1 = relative;
                    runnables.add(() -> {
                        flywheelEndpoint.innerCreateStructure(level, finalRelative1, side.getOpposite(), player);
                        linkedParts.add(getMasterPose(level,finalRelative1));
                    });
                } else {
                    success = false;
                }
                break;
            }
        }

        if (success) {
            runnables.forEach(Runnable::run);
            runnables.clear();

            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof IMultiblockBE<?> be) {
                EndPointLogic.State state1 = (EndPointLogic.State) be.getHelper().getContext().getState();
                state1.isMaster = true;
                state1.linkedParts = linkedParts;
                BlockPos masterPose = be.getHelper().getContext().getLevel().getAbsoluteOrigin();

                for (int i = 0; i <= length; i++) {
                    BlockPos partPos = pos.relative(side.getOpposite(), i + 3);
                    BlockEntity partEntity = level.getBlockEntity(partPos);
                    if (partEntity instanceof IMultiblockBE<?> partBe) {
                        IMultiblockState state = partBe.getHelper().getContext().getState();
                        if (state instanceof FlyWheelPart flyWheelPart) {
                            flyWheelPart.setMasterPos(masterPose);
                            flyWheelPart.linkedParts = linkedParts;
                        }
                    }
                }
            }
        }
        return success;
    }

    public BlockPos getMasterPose(Level level, BlockPos blockPos) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof IMultiblockBE<?> be) {
            return be.getHelper().getContext().getLevel().toAbsolute(be.getHelper().getPositionInMB());
        } else {
            return blockPos;
        }
    }

    public void innerCreateStructure(Level world, BlockPos pos, Direction side, Player player) {
        super.createStructure(world, pos, side, player);
    }
}
