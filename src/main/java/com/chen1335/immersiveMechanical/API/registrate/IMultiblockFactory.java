package com.chen1335.immersiveMechanical.API.registrate;

import blusunrize.immersiveengineering.api.multiblocks.BlockMatcher;
import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface IMultiblockFactory {
    IETemplateMultiblock create(ResourceLocation loc,
                                BlockPos masterFromOrigin,
                                BlockPos triggerFromOrigin,
                                BlockPos size,
                                MultiblockRegistration<?> logic,
                                List<BlockMatcher.MatcherPredicate> additionalPredicates,
                                float manualScale
    );
}
