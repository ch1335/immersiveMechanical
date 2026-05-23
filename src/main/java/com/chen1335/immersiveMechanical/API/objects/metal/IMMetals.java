package com.chen1335.immersiveMechanical.API.objects.metal;

import blusunrize.immersiveengineering.common.items.IEBaseItem;
import com.chen1335.immersiveMechanical.definitions.IMItems;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;

import java.util.EnumMap;
import java.util.Map;

public enum IMMetals {
    CHROME("chrome"),
    NICHROME("nichrome");
    public static final Map<IMMetals, Map<IMMetalTypes, ItemEntry<IEBaseItem>>> METALS = new EnumMap<>(IMMetals.class);

    public static final Map<IMMetals, Map<IMMetalTypes, TagKey<Item>>> METAL_TAGS = new EnumMap<>(IMMetals.class);
    private final String name;

    IMMetals(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ItemEntry<IEBaseItem> getMetal(IMMetalTypes metalTypes) {
        return IMMetals.METALS.get(this).get(metalTypes);
    }

    public TagKey<Item> getTag(IMMetalTypes metalTypes) {
        return IMMetals.METAL_TAGS.get(this).get(metalTypes);
    }

    public static void register(IEventBus modEventBus) {
        for (IMMetals metals : IMMetals.values()) {
            for (IMMetalTypes metalTypes : IMMetalTypes.values()) {
                Map<IMMetalTypes, ItemEntry<IEBaseItem>> map = METALS.computeIfAbsent(metals, metals1 -> new EnumMap<>(IMMetalTypes.class));
                ItemEntry<IEBaseItem> simple = IMItems.simple(metalTypes.getFormat().formatted(metals.name));
                map.put(metalTypes, simple);

                Map<IMMetalTypes, TagKey<Item>> tagKeyMap = METAL_TAGS.computeIfAbsent(metals, metals1 -> new EnumMap<>(IMMetalTypes.class));
                tagKeyMap.put(metalTypes, metalTypes.getTag(metals.name));
            }
        }
    }

}
