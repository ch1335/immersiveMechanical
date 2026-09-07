package com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks;

import blusunrize.immersiveengineering.api.multiblocks.BlockMatcher;
import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockBE;
import blusunrize.immersiveengineering.api.utils.DirectionUtils;
import com.chen1335.registrate.SimpleMultiblock;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;

public class TestAbleTemplateMultiblock extends SimpleMultiblock {


    public TestAbleTemplateMultiblock(ResourceLocation loc, BlockPos masterFromOrigin, BlockPos triggerFromOrigin, BlockPos size, MultiblockRegistration<?> logic, List<BlockMatcher.MatcherPredicate> additionalPredicates, float manualScale) {
        super(loc, masterFromOrigin, triggerFromOrigin, size, logic, additionalPredicates, manualScale);
    }

    public boolean canForm(Level world, BlockPos pos, Direction side, Player player) {
        Rotation rot = DirectionUtils.getRotationBetweenFacings(Direction.NORTH, side.getOpposite());
        if (rot == null)
            return false;
        List<StructureTemplate.StructureBlockInfo> structure = getStructure(world);
        mirrorLoop:
        for (Mirror mirror : getPossibleMirrorStates()) {
            StructurePlaceSettings placeSet = new StructurePlaceSettings().setMirror(mirror).setRotation(rot);
            BlockPos origin = pos.subtract(StructureTemplate.calculateRelativePosition(placeSet, triggerFromOrigin));
            for (StructureTemplate.StructureBlockInfo info : structure) {
                BlockPos realRelPos = StructureTemplate.calculateRelativePosition(placeSet, info.pos());
                BlockPos here = origin.offset(realRelPos);

                BlockState expected = applyToState(info.state(), mirror, rot);
                BlockState inWorld = world.getBlockState(here);
                if (!BlockMatcher.matches(expected, inWorld, world, here, additionalPredicates).isAllow())
                    continue mirrorLoop;
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

    private List<Mirror> getPossibleMirrorStates() {
        if (canBeMirrored())
            return ImmutableList.of(Mirror.NONE, Mirror.FRONT_BACK);
        else
            return ImmutableList.of(Mirror.NONE);
    }


}
