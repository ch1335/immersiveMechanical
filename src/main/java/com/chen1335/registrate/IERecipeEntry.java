package com.chen1335.registrate;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.IERecipeTypes;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class IERecipeEntry<T extends Recipe<?>> extends RegistryEntry<RecipeType<?>, RecipeType<T>> {

    private final IERecipeTypes.TypeWithClass<T> type;
    private final RegistryEntry<RecipeSerializer<?>, IERecipeSerializer<T>> serializerRegistryEntry;

    public IERecipeEntry(AbstractRegistrate<?> owner, DeferredHolder<RecipeType<?>, RecipeType<T>> key, Class<T> clazz, RegistryEntry<RecipeSerializer<?>, IERecipeSerializer<T>> serializerRegistryEntry) {
        super(owner, key);
        this.serializerRegistryEntry = serializerRegistryEntry;
        this.type = new IERecipeTypes.TypeWithClass<>(key, clazz);
    }

    public IERecipeTypes.TypeWithClass<T> getIEType() {
        return type;
    }

    public IERecipeSerializer<T> getSerializer() {
        return serializerRegistryEntry.get();
    }
}
