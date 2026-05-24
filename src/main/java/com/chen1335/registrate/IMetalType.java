package com.chen1335.registrate;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface IMetalType {
    String format(String name);

    String getFormat();

    TagKey<Item> getTag(String name);

    TagKey<Item> getTypTag();

    String typeName();
}
