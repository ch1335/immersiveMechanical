package com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks;

import blusunrize.immersiveengineering.api.multiblocks.BlockMatcher;
import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import com.chen1335.registrate.SimpleMultiblock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class DEMOTemplate extends SimpleMultiblock {
    public DEMOTemplate(ResourceLocation loc, BlockPos masterFromOrigin, BlockPos triggerFromOrigin, BlockPos size, MultiblockRegistration<?> logic, List<BlockMatcher.MatcherPredicate> additionalPredicates, float manualScale) {
        super(loc, masterFromOrigin, triggerFromOrigin, size, logic, additionalPredicates, manualScale);
    }

    @Override
    public boolean isBlockTrigger(BlockState state, Direction side, @Nullable Level world)
    {
        return false;
    }

    @Override
    public boolean createStructure(Level world, BlockPos pos, Direction side, Player player)
    {
        return false;
    }
}
