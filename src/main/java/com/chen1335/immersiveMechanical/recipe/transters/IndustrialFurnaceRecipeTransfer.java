package com.chen1335.immersiveMechanical.recipe.transters;

import com.chen1335.immersiveMechanical.definitions.IMRecipe;
import com.chen1335.immersiveMechanical.recipe.IndustrialFurnaceRecipe;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;

import java.util.HashMap;
import java.util.Map;

public class IndustrialFurnaceRecipeTransfer implements IRecipeTransfer {

    private final Map<Ingredient, Pair<RecipeHolder<IndustrialFurnaceRecipe>, Runnable>> captured = new HashMap<>();

    public static IndustrialFurnaceRecipeTransfer INSTANCE = new IndustrialFurnaceRecipeTransfer();

    @Override
    public void apply(HolderLookup.Provider provider, ResourceLocation resourcelocation, ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> byTypeBuilder, ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>> byNameBuilder, Recipe<?> recipe) {
        if (recipe.getType() == RecipeType.SMELTING || recipe.getType() == RecipeType.BLASTING) {
            boolean isFood = recipe.getResultItem(provider).has(DataComponents.FOOD);
            if (isFood) {
                return;
            }
            IndustrialFurnaceRecipe industrialFurnaceRecipe = IndustrialFurnaceRecipe.fromSmeltingRecipe((AbstractCookingRecipe) recipe, provider);
            ResourceLocation resourceLocation = resourcelocation.withPrefix("industrial_furnace/");
            RecipeHolder<IndustrialFurnaceRecipe> recipeHolder = new RecipeHolder<>(resourceLocation, industrialFurnaceRecipe);

            Pair<RecipeHolder<IndustrialFurnaceRecipe>, Runnable> pair = captured.get(industrialFurnaceRecipe.input());
            if (pair == null) {
                captured.put(industrialFurnaceRecipe.input(), Pair.of(recipeHolder, () -> {
                    byTypeBuilder.put(IMRecipe.INDUSTRIAL_FURNACE.get(), recipeHolder);
                    byNameBuilder.put(resourceLocation, recipeHolder);
                }));
            } else if (pair.getFirst().value().getBaseTime() > industrialFurnaceRecipe.getBaseTime()) {
                captured.put(industrialFurnaceRecipe.input(), Pair.of(recipeHolder, () -> {
                    byTypeBuilder.put(IMRecipe.INDUSTRIAL_FURNACE.get(), recipeHolder);
                    byNameBuilder.put(resourceLocation, recipeHolder);
                }));
            }

        }
    }

    @Override
    public void onFinalBuild() {
        captured.values().forEach(pair -> pair.getSecond().run());
        captured.clear();
    }
}
