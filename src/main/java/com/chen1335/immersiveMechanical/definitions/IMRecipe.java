package com.chen1335.immersiveMechanical.definitions;

import com.chen1335.immersiveMechanical.recipe.IndustrialFurnaceRecipe;
import com.chen1335.immersiveMechanical.recipe.PyrolyseOvenRecipe;
import com.chen1335.registrate.IERecipeEntry;

import static com.chen1335.immersiveMechanical.ImmersiveMechanical.REGISTRATE;

public class IMRecipe {
    public static final IERecipeEntry<IndustrialFurnaceRecipe> INDUSTRIAL_FURNACE = REGISTRATE.recipeType(
            "industrial_furnace",
            IndustrialFurnaceRecipe.class,
            IndustrialFurnaceRecipe.Serializer::new
    ).register();

    public static final IERecipeEntry<PyrolyseOvenRecipe> PYROLYSE_OVEN = REGISTRATE.recipeType(
            "pyrolyse_oven",
            PyrolyseOvenRecipe.class,
            PyrolyseOvenRecipe.Serializer::new
    ).register();

    public static void init() {


    }

}
