package com.chen1335.immersiveMechanical.API.tags;

import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.api.utils.TagUtils;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface IMItemTags {
    TagKey<Item> ORES_CHROME = TagUtils.createItemWrapper(IETags.getOre("chrome"));
    TagKey<Item> DEEPSLATE_CHROME = TagUtils.createItemWrapper(IETags.getOre("deepslate_chrome"));
    TagKey<Item> RAW_CHROME = TagUtils.createItemWrapper(IETags.getRawOre("chrome"));
}
