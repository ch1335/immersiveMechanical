package com.chen1335.immersiveMechanical.kubejs.recipe;

import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import blusunrize.immersiveengineering.api.crafting.StackWithChance;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import com.chen1335.immersiveEngineeringJs.kubejs.recipe.Schemas;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.FluidStackComponent;
import dev.latvian.mods.kubejs.recipe.component.ListRecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.IntBounds;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;

public interface PyrolyseOvenSchema {
    RecipeKey<IngredientWithSize> INPUT = Schemas.INGREDIENT_WITH_SIZE.inputKey("input");
    RecipeKey<TagOutput> RESULT = Schemas.TAG_OUTPUT.outputKey("result");
    RecipeKey<List<StackWithChance>> SECONDARY_OUTPUTS = ListRecipeComponent.create(
            Schemas.STACK_WITH_CHANCE, false, false, IntBounds.OPTIONAL, Optional.empty()
    ).outputKey("secondary_outputs").optional(List.of()).alwaysWrite();
    RecipeKey<FluidStack> FLUID_OUTPUT = FluidStackComponent.OPTIONAL_FLUID_STACK.outputKey("fluid_output").optional(FluidStack.EMPTY).alwaysWrite();
    RecipeKey<Integer> TIME = NumberComponent.IntRange.INT.inputKey("time").optional(200).alwaysWrite();
    RecipeKey<Integer> ENERGY = NumberComponent.IntRange.INT.inputKey("energy");

    RecipeSchema SCHEMA = new RecipeSchema(INPUT, RESULT, ENERGY, TIME, FLUID_OUTPUT, SECONDARY_OUTPUTS);
}
