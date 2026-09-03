package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.flywheel;

import blusunrize.immersiveengineering.api.multiblocks.BlockMatcher;
import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockBE;
import blusunrize.immersiveengineering.api.utils.DirectionUtils;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks.TestAbleTemplateMultiblock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FlyWheelTemplate extends TestAbleTemplateMultiblock {
    public FlyWheelTemplate(ResourceLocation loc, BlockPos masterFromOrigin, BlockPos triggerFromOrigin, BlockPos size, MultiblockRegistration<?> logic, List<BlockMatcher.MatcherPredicate> additionalPredicates, float manualScale) {
        super(loc, masterFromOrigin, triggerFromOrigin, size, logic, additionalPredicates, manualScale);
    }

    @Override
    public boolean isBlockTrigger(BlockState state, Direction d, @NotNull Level world) {
        return super.isBlockTrigger(state, d, world);
    }

    @Override
    public boolean canForm(Level world, BlockPos pos, Direction side, Player player) {
        Rotation rot = DirectionUtils.getRotationBetweenFacings(Direction.NORTH, side.getOpposite());
        if (rot == null) return false;
        StructurePlaceSettings placeSet = new StructurePlaceSettings().setMirror(Mirror.NONE).setRotation(rot);
        BlockPos origin = pos.subtract(StructureTemplate.calculateRelativePosition(placeSet, triggerFromOrigin));
        BlockPos masterPos = withSettingsAndOffset(origin, masterFromOrigin, Mirror.NONE, rot);
        Block needBlock = null;
        boolean flag = false;
        for (StructureTemplate.StructureBlockInfo structureBlockInfo : getStructure(world)) {
            BlockPos actualPos = withSettingsAndOffset(origin, structureBlockInfo.pos(), Mirror.NONE, rot);
            if (!actualPos.equals(masterPos)) {
                Block block = world.getBlockState(actualPos).getBlock();
                if (needBlock == null) {
                    needBlock = block;
                } else if (block != needBlock) {
                    flag = true;
                    break;
                }
            }
        }
        if (flag) {
            return false;
        }

        return super.canForm(world, pos, side, player);
    }

    @Override
    public boolean createStructure(Level level, BlockPos pos, Direction side, Player player) {
        Block block = level.getBlockState(pos.below(1)).getBlock();
        boolean structure = super.createStructure(level, pos, side, player);
        if (structure) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof IMultiblockBE<?> be && be.getHelper().getState() instanceof FlyWheelLogic.State state) {
                state.material = block;
            }
        }
        return structure;
    }
}
