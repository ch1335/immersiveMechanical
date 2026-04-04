package com.chen1335.immersiveMechanical.API.objects.metal;

import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.api.utils.TagUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;

import java.util.function.Function;

public enum IMMetalTypes {
    INGOTS("ingot_%s", Tags.Items.INGOTS, IETags::getIngot),
    NUGGETS("nugget_%s", Tags.Items.NUGGETS, IETags::getNugget),
    DUSTS("dust_%s", Tags.Items.DUSTS, IETags::getDust),
    PLATES("plate_%s", IETags.plates, IETags::getPlate),
    STICKS("stick_%s", Tags.Items.RODS, IETags::getRod);

    private final String format;
    private final TagKey<Item> typTag;
    private final Function<String, ResourceLocation> tagGetter;


    IMMetalTypes(String format, TagKey<Item> typTag, Function<String, ResourceLocation> tagGetter) {
        this.format = format;
        this.typTag = typTag;
        this.tagGetter = tagGetter;
    }

    public String format(String name){
        return format.formatted(name);
    }

    public String getFormat() {
        return format;
    }

    public TagKey<Item> getTag(String name) {
        return TagUtils.createItemWrapper(tagGetter.apply(name));
    }

    public TagKey<Item> getTypTag() {
        return typTag;
    }
}
