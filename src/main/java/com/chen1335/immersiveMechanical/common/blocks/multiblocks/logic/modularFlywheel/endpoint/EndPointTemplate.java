package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.endpoint;

import blusunrize.immersiveengineering.api.multiblocks.BlockMatcher;
import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockBE;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlyWheelPart;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks.TestAbleTemplateMultiblock;
import com.chen1335.immersiveMechanical.config.IMServerConfig;
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
        TestAbleTemplateMultiblock bearing = (TestAbleTemplateMultiblock) IMMultiblocks.BEARING.multiblock();
        EndPointTemplate flywheelEndpoint = (EndPointTemplate) IMMultiblocks.FLYWHEEL_ENDPOINT.multiblock();
        int maxLength = IMServerConfig.MACHINES.flywheel_maximum_length.get();
        List<Runnable> runnables = new ArrayList<>();
        List<BlockPos> linkedParts = new ArrayList<>();
        if (canForm(level, pos, side, player)) {
            runnables.add(() -> {
                this.innerCreateStructure(level, pos, side, player);
                linkedParts.add(getMasterPose(level, pos));
            });
        } else {
            return false;
        }

        int length = 0;

        for (int i = 0; i <= maxLength; i++) {
            BlockPos otherEndPoint = pos.relative(side.getOpposite(), i + 5);
            if (flywheelEndpoint.canForm(level, otherEndPoint, side.getOpposite(), player)) {
                length = i;
                runnables.add(() -> {
                    flywheelEndpoint.innerCreateStructure(level, otherEndPoint, side.getOpposite(), player);
                    linkedParts.add(getMasterPose(level, otherEndPoint));
                });
                break;
            } else if (i == maxLength) {
                return false;
            }
        }

        for (int i = 0; i < length; i++) {
            BlockPos relative = pos.relative(side.getOpposite(), i + 3);
            if (flywheel.canForm(level, relative, side, player)) {
                runnables.add(() -> {
                    flywheel.createStructure(level, relative, side, player);
                    linkedParts.add(getMasterPose(level, relative));
                });
            } else if (bearing.canForm(level, relative, side, player)) {
                runnables.add(() -> {
                    bearing.createStructure(level, relative, side, player);
                    linkedParts.add(getMasterPose(level, relative));
                });
            } else {
                return false;
            }
        }

        if (length > 0) {
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
            return true;
        }
        return false;
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
