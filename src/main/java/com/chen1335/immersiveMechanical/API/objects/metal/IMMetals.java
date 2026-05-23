package com.chen1335.immersiveMechanical.API.objects.metal;

import com.chen1335.immersiveMechanical.definitions.IMItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.EnumMap;
import java.util.Map;

public enum IMMetals {
    CHROME("chrome"),
    NICHROME("nichrome");
    public static final Map<IMMetals, Map<IMMetalTypes, DeferredItem<Item>>> METALS = new EnumMap<>(IMMetals.class);

    public static final Map<IMMetals, Map<IMMetalTypes, TagKey<Item>>> METAL_TAGS = new EnumMap<>(IMMetals.class);
    private final String name;

    IMMetals(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public DeferredItem<Item> getMetal(IMMetalTypes metalTypes) {
        return IMMetals.METALS.get(this).get(metalTypes);
    }

    public TagKey<Item> getTag(IMMetalTypes metalTypes) {
        return IMMetals.METAL_TAGS.get(this).get(metalTypes);
    }

    public static void register(IEventBus modEventBus) {
        for (IMMetals metals : IMMetals.values()) {
            for (IMMetalTypes metalTypes : IMMetalTypes.values()) {
                Map<IMMetalTypes, DeferredItem<Item>> map = METALS.computeIfAbsent(metals, metals1 -> new EnumMap<>(IMMetalTypes.class));
                DeferredItem<Item> simple = IMItems.simple(metalTypes.getFormat().formatted(metals.name));
                map.put(metalTypes, simple);

                Map<IMMetalTypes, TagKey<Item>> tagKeyMap = METAL_TAGS.computeIfAbsent(metals, metals1 -> new EnumMap<>(IMMetalTypes.class));
                tagKeyMap.put(metalTypes, metalTypes.getTag(metals.name));
            }
        }
    }

}
