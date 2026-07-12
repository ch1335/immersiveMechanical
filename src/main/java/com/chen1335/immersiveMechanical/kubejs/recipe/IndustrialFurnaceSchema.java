package com.chen1335.immersiveMechanical.kubejs.recipe;

import blusunrize.immersiveengineering.api.crafting.TagOutput;
import com.chen1335.immersiveEngineeringJs.kubejs.recipe.Schemas;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.crafting.Ingredient;

public interface IndustrialFurnaceSchema {
    RecipeKey<Ingredient> INPUT = IngredientComponent.INGREDIENT.inputKey("input");
    RecipeKey<TagOutput> OUTPUT = Schemas.TAG_OUTPUT.outputKey("output");
    RecipeKey<Integer> TIME = NumberComponent.IntRange.INT.inputKey("time").alwaysWrite();
    RecipeKey<Integer> ENERGY = NumberComponent.IntRange.INT.inputKey("energy").alwaysWrite();

    RecipeSchema SCHEMA = new RecipeSchema(INPUT, OUTPUT, TIME, ENERGY);
}
