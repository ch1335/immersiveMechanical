package com.chen1335.registrate.builder;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import com.chen1335.registrate.IERecipeEntry;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RecipeBuilder<S extends RecipeSerializer<T>,T extends Recipe<?>> extends AbstractBuilder<RecipeType<?>, RecipeType<T>, AbstractRegistrate<?>, RecipeBuilder<S,T>> {

    private final Class<T> clazz;
    private final Supplier<S> recipeSerializerSupplier;

    public RecipeBuilder(AbstractRegistrate<?> owner, AbstractRegistrate<?> parent, String name, BuilderCallback callback, Class<T> clazz, Supplier<S> serializerSupplier) {
        super(owner, parent, name, callback, Registries.RECIPE_TYPE);
        this.clazz = clazz;
        this.recipeSerializerSupplier = serializerSupplier;
    }

    @Override
    protected @NonnullType RecipeType<T> createEntry() {
        return new RecipeType<>() {
        };
    }

    @Override
    protected RegistryEntry<RecipeType<?>, RecipeType<T>> createEntryWrapper(DeferredHolder<RecipeType<?>, RecipeType<T>> delegate) {
        RegistryEntry<RecipeSerializer<?>, S> register = getOwner().entry(getName(), builderCallback -> new RecipeSerializerBuilder<>(getOwner(), getParent(), getName(), builderCallback, recipeSerializerSupplier)).register();
        return new IERecipeEntry<>(getOwner(), delegate, clazz, register);
    }

    @Override
    public IERecipeEntry<S,T> register() {
        return (IERecipeEntry<S,T>) super.register();
    }

    public static class RecipeSerializerBuilder<S extends RecipeSerializer<T>,T extends Recipe<?>> extends AbstractBuilder<RecipeSerializer<?>, S, AbstractRegistrate<?>, RecipeSerializerBuilder<S,T>> {

        private final Supplier<S> supplier;

        public RecipeSerializerBuilder(AbstractRegistrate<?> owner, AbstractRegistrate<?> parent, String name, BuilderCallback callback, Supplier<S> supplier) {
            super(owner, parent, name, callback, Registries.RECIPE_SERIALIZER);
            this.supplier = supplier;
        }


        @Override
        protected @NonnullType S createEntry() {
            return supplier.get();
        }
    }
}
