package com.chen1335.immersiveMechanical.recipe.transters;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

public interface IRecipeTransfer {
    void apply(HolderLookup.Provider provider, ResourceLocation resourcelocation, ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> byTypeBuilder, ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>> byNameBuilder, Recipe<?> recipe);

    void onFinalBuild();
}
