package com.chen1335.immersiveMechanical.definitions;

import blusunrize.immersiveengineering.api.multiblocks.BlockMatcher;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil.CoilInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class MatcherPredicates {
    public static BlockMatcher.Result coil(BlockState expected, BlockState found, @Nullable Level world, @Nullable BlockPos pos){
        if (CoilInfo.COIL_INFO_MAP.containsKey(found.getBlock())) {
            return BlockMatcher.Result.allow(2);
        }
        return BlockMatcher.Result.DEFAULT;
    }
}
