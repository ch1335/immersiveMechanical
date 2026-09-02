package com.chen1335.immersiveMechanical.definitions;

import blusunrize.immersiveengineering.api.multiblocks.BlockMatcher;
import com.chen1335.immersiveMechanical.API.objects.IMRegistries;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil.CoilInfo;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlywheelMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Optional;

public class MatcherPredicates {
    public static BlockMatcher.Result coil(BlockState expected, BlockState found, @Nullable Level level, @Nullable BlockPos pos) {
        if (CoilInfo.COIL_INFO_MAP.containsKey(found.getBlock())) {
            return BlockMatcher.Result.allow(2);
        }
        return BlockMatcher.Result.DEFAULT;
    }

    public static BlockMatcher.Result flywheelMaterial(BlockState expected, BlockState found, @Nullable Level level, @Nullable BlockPos pos) {
        if (level != null && !found.isAir()) {
            Optional<Holder.Reference<FlywheelMaterial>> any = level.holderLookup(IMRegistries.FLYWHEEL_MATERIAL).listElements().filter(holder -> holder.value().holderSet().contains(found.getBlockHolder())).findAny();
            if (any.isPresent()) {
                return BlockMatcher.Result.allow(2);
            }
        }
        return BlockMatcher.Result.DEFAULT;
    }
}
