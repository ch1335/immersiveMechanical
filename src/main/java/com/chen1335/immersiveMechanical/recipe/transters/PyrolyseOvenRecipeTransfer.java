package com.chen1335.immersiveMechanical.recipe.transters;

import blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe;
import blusunrize.immersiveengineering.api.crafting.IERecipeTypes;
import com.chen1335.immersiveMechanical.definitions.IMRecipe;
import com.chen1335.immersiveMechanical.recipe.PyrolyseOvenRecipe;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

public class PyrolyseOvenRecipeTransfer implements IRecipeTransfer {
    public static PyrolyseOvenRecipeTransfer INSTANCE = new PyrolyseOvenRecipeTransfer();
    @Override
    public void apply(HolderLookup.Provider provider, ResourceLocation resourcelocation, ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> byTypeBuilder, ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>> byNameBuilder, Recipe<?> recipe) {
        if (recipe.getType() == IERecipeTypes.COKE_OVEN.get()) {
            PyrolyseOvenRecipe pyrolyseOvenRecipe = PyrolyseOvenRecipe.fromCokeOvenRecipe((CokeOvenRecipe) recipe);
            ResourceLocation resourceLocation = resourcelocation.withPrefix("pyrolyse_oven/");
            RecipeHolder<PyrolyseOvenRecipe> recipeHolder = new RecipeHolder<>(resourceLocation, pyrolyseOvenRecipe);
            byTypeBuilder.put(IMRecipe.PYROLYSE_OVEN.get(), recipeHolder);
            byNameBuilder.put(resourceLocation, recipeHolder);
        }
    }

    @Override
    public void onFinalBuild() {

    }
}
