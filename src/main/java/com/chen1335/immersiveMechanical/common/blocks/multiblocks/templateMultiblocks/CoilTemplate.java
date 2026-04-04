package com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.multiblocks.BlockMatcher;
import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockBEHelper;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockBE;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockBlockEntityDummy;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockBlockEntityMaster;
import blusunrize.immersiveengineering.api.utils.DirectionUtils;
import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.interfaces.MBMemorizeStructure;
import blusunrize.immersiveengineering.common.util.IELogger;
import com.chen1335.immersiveMechanical.API.objects.IMAttachmentTypes;
import com.chen1335.immersiveMechanical.attachmentDatas.IMBEAttachmentData;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil.CoilInfo;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil.CoilLogic;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;


public class CoilTemplate extends IETemplateMultiblock {
    private final MultiblockRegistration<CoilLogic.State> logic;


    public CoilTemplate(ResourceLocation loc, MultiblockRegistration<CoilLogic.State> logic) {
        super(loc, new BlockPos(1, 0, 2), new BlockPos(1, 0, 2), new BlockPos(3, 1, 3), logic, ImmutableList.of((expected, found, world, pos) -> {
            if (CoilInfo.COIL_INFO_MAP.containsKey(found.getBlock())) {
                return BlockMatcher.Result.allow(2);
            }
            return BlockMatcher.Result.DEFAULT;
        }));
        this.logic = logic;
    }

    @Override
    public float getManualScale() {
        return 9;
    }

    public MultiblockRegistration<CoilLogic.State> getLogic() {
        return logic;
    }

    @Override
    protected void form(Level world, BlockPos pos, Rotation rot, Mirror mirror, Direction sideHit) {
        BlockPos masterPos = withSettingsAndOffset(pos, masterFromOrigin, mirror, rot);
        Block needBlock = null;
        boolean flag = false;
        for (StructureTemplate.StructureBlockInfo structureBlockInfo : getStructure(world)) {
            BlockPos actualPos = withSettingsAndOffset(pos, structureBlockInfo.pos(), mirror, rot);
            Block block = world.getBlockState(actualPos).getBlock();
            if (needBlock == null) {
                needBlock = block;
            } else if (block != needBlock) {
                flag = true;
                break;
            }
        }

        if (flag) return;

        for (StructureTemplate.StructureBlockInfo structureBlockInfo : getStructure(world)) {
            if (structureBlockInfo.pos().equals(masterFromOrigin)) {
                BlockPos actualPos = withSettingsAndOffset(pos, structureBlockInfo.pos(), mirror, rot);
                replaceStructureBlock(structureBlockInfo, world, actualPos, mirror != Mirror.NONE, sideHit, actualPos.subtract(masterPos));
                break;
            }
        }

        for (StructureTemplate.StructureBlockInfo block : getStructure(world)) {
            if (!block.pos().equals(masterFromOrigin)) {
                BlockPos actualPos = withSettingsAndOffset(pos, block.pos(), mirror, rot);
                replaceStructureBlock(block, world, actualPos, mirror != Mirror.NONE, sideHit, actualPos.subtract(masterPos));
            }
        }
    }


    @Override
    protected void replaceStructureBlock(StructureTemplate.StructureBlockInfo info, Level world, BlockPos actualPos, boolean mirrored, Direction clickDirection, Vec3i offsetFromMaster) {
        BlockState newState = logic.block().get().defaultBlockState();
        newState = newState.setValue(IEProperties.MULTIBLOCKSLAVE, !offsetFromMaster.equals(Vec3i.ZERO));
        if (newState.hasProperty(IEProperties.ACTIVE))
            newState = newState.setValue(IEProperties.ACTIVE, false);
        if (newState.hasProperty(IEProperties.MIRRORED))
            newState = newState.setValue(IEProperties.MIRRORED, mirrored);
        if (newState.hasProperty(IEProperties.FACING_HORIZONTAL))
            newState = newState.setValue(IEProperties.FACING_HORIZONTAL, clickDirection.getOpposite());
        final BlockState oldState = world.getBlockState(actualPos);
        world.setBlock(actualPos, newState, 0);
        BlockEntity curr = world.getBlockEntity(actualPos);
        if (curr instanceof MultiblockBlockEntityDummy<?> dummy)
            dummy.getHelper().setPositionInMB(info.pos());
        else if (!(curr instanceof MultiblockBlockEntityMaster<?>))
            IELogger.logger.error("Expected MB TE at {} during placement", actualPos);

        if (curr != null) {
            IMBEAttachmentData data = curr.getData(IMAttachmentTypes.BE_ATTACHMENT);
            data.originalBlock = oldState;

            IMultiblockBEHelper<IMultiblockState> helper = ((IMultiblockBE<IMultiblockState>) curr).getHelper();
            if (helper.getMultiblock().logic() instanceof MBMemorizeStructure<IMultiblockState> memo)
                memo.setMemorizedBlockState(helper.getState(), info.pos(), oldState);
        }

        final LevelChunk chunk = world.getChunkAt(actualPos);
        world.markAndNotifyBlock(actualPos, chunk, oldState, newState, Block.UPDATE_ALL, 512);
    }

    @Override
    public void disassemble(@NotNull Level level, @NotNull BlockPos origin, boolean mirrored, @NotNull Direction clickDirectionAtCreation) {
        Mirror mirror = mirrored ? Mirror.FRONT_BACK : Mirror.NONE;
        Rotation rot = DirectionUtils.getRotationBetweenFacings(Direction.NORTH, clickDirectionAtCreation);
        Preconditions.checkNotNull(rot);
        for (StructureTemplate.StructureBlockInfo info : getStructure(level)) {
            BlockPos actualPos = withSettingsAndOffset(origin, info.pos(), mirror, rot);
            prepareBlockForDisassembly(level, actualPos);
            BlockEntity blockEntity = level.getBlockEntity(actualPos);
            BlockState blockState = null;
            if (blockEntity != null) {
                IMBEAttachmentData data = blockEntity.getData(IMAttachmentTypes.BE_ATTACHMENT);
                blockState = data.originalBlock;
            }
            level.setBlockAndUpdate(actualPos, applyToState(blockState != null ? blockState : info.state(), mirror, rot));
        }
    }
}
