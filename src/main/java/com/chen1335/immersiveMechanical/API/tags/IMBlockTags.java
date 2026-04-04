package com.chen1335.immersiveMechanical.API.tags;

import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.api.utils.TagUtils;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public interface IMBlockTags {
    TagKey<Block> ORES_CHROME = TagUtils.createBlockWrapper(IETags.getOre("chrome"));

}
