package com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks;

import blusunrize.immersiveengineering.api.multiblocks.BlockMatcher;
import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.api.utils.DirectionUtils;
import com.chen1335.immersiveMechanical.common.blockEntities.LargeBatteryCoreTile;
import com.chen1335.registrate.SimpleMultiblock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;

public class LargeBatteryTemplate extends SimpleMultiblock {
    /**
     * 核心方块相对模板原点的坐标,该位置必须是 LargeBatteryCoreTile 才能形成多方块结构
     */
    private static final BlockPos CORE = new BlockPos(1, 2, 1);

    public LargeBatteryTemplate(ResourceLocation loc, BlockPos masterFromOrigin, BlockPos triggerFromOrigin, BlockPos size, MultiblockRegistration<?> logic, List<BlockMatcher.MatcherPredicate> additionalPredicates, float manualScale) {
        super(loc, masterFromOrigin, triggerFromOrigin, size, logic, additionalPredicates, manualScale);
    }

    @Override
    public boolean createStructure(Level world, BlockPos pos, Direction side, Player player) {
        // 按照触发方向计算结构的旋转,与父类 createStructure 保持一致
        Rotation rot = DirectionUtils.getRotationBetweenFacings(Direction.NORTH, side.getOpposite());
        if (rot == null) {
            return false;
        }
        // 本多方块不可镜像,只有 Mirror.NONE
        Mirror mirror = Mirror.NONE;
        StructurePlaceSettings settings = new StructurePlaceSettings().setMirror(mirror).setRotation(rot);
        // origin = 触发方块世界坐标 - 触发方块相对原点的(经旋转后)偏移
        BlockPos origin = pos.subtract(StructureTemplate.calculateRelativePosition(settings, triggerFromOrigin));
        // (1,2,1) 处的实际世界坐标
        BlockPos corePosWorld = withSettingsAndOffset(origin, CORE, mirror, rot);
        // 该位置的方块实体不是 LargeBatteryCoreTile 时,拒绝形成多方块结构
        if (!(world.getBlockEntity(corePosWorld) instanceof LargeBatteryCoreTile)) {
            return false;
        }
        return super.createStructure(world, pos, side, player);
    }
}
