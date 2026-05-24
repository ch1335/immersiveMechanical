package com.chen1335.registrate;

import com.mojang.logging.LogUtils;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;

import java.util.Map;

public class MetalDefinition {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final String name;
    private final String modid;
    private final Map<IMetalType, ItemEntry<Item>> typeItemMap;

    public MetalDefinition(String name, String modid, Map<IMetalType, ItemEntry<Item>> typeItemMap) {
        this.name = name;
        this.modid = modid;
        this.typeItemMap = typeItemMap;
    }


    public Item getItem(IMetalType type) {
        ItemEntry<Item> itemItemEntry = typeItemMap.get(type);
        if (itemItemEntry == null) {
            LOGGER.error("can not find metal type {} from {}:{}", type.typeName(), modid, name);
            return Items.AIR;
        }
        return itemItemEntry.asItem();
    }

    public TagKey<Item> getTag(IMetalType type) {
        return type.getTag(name);
    }

    public String getName() {
        return name;
    }
}
