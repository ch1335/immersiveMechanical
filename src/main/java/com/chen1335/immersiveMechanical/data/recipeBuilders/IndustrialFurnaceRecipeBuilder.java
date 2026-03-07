package com.chen1335.immersiveMechanical.data.recipeBuilders;

import blusunrize.immersiveengineering.api.crafting.TagOutput;
import com.chen1335.immersiveMechanical.recipe.IndustrialFurnaceRecipe;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public class IndustrialFurnaceRecipeBuilder implements RecipeBuilder {
    private final Ingredient input;
    private final TagOutput output;
    private final int time;
    private final int energy;

    public IndustrialFurnaceRecipeBuilder(Ingredient input, TagOutput output, int time, int energy) {
        this.input = input;
        this.output = output;
        this.time = time;
        this.energy = energy;
    }

    public static IndustrialFurnaceRecipeBuilder builder(Ingredient input, TagOutput output, int time, int energy) {
        return new IndustrialFurnaceRecipeBuilder(input, output, time, energy);
    }

    public void build(RecipeOutput recipeOutput) {
        this.save(recipeOutput);
    }


    public void build(RecipeOutput recipeOutput, ResourceLocation resourceLocation) {
        this.save(recipeOutput, resourceLocation);
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return output.get().getItem();
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        recipeOutput.accept(id.withPrefix("industrial_furnace/"), new IndustrialFurnaceRecipe(input, output, time, energy), null);
    }
}
