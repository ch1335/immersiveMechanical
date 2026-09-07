package com.chen1335.immersiveMechanical.definitions;

import com.chen1335.immersiveMechanical.recipe.IndustrialFurnaceRecipe;
import com.chen1335.immersiveMechanical.recipe.LandmineDisguiseRecipe;
import com.chen1335.immersiveMechanical.recipe.PyrolyseOvenRecipe;
import com.chen1335.registrate.IERecipeEntry;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

import static com.chen1335.immersiveMechanical.ImmersiveMechanical.REGISTRATE;

public class IMRecipe {
    public static final IERecipeEntry<IndustrialFurnaceRecipe.Serializer, IndustrialFurnaceRecipe> INDUSTRIAL_FURNACE = REGISTRATE.recipeType(
            "industrial_furnace",
            IndustrialFurnaceRecipe.class,
            IndustrialFurnaceRecipe.Serializer::new
    ).register();

    public static final IERecipeEntry<PyrolyseOvenRecipe.Serializer, PyrolyseOvenRecipe> PYROLYSE_OVEN = REGISTRATE.recipeType(
            "pyrolyse_oven",
            PyrolyseOvenRecipe.class,
            PyrolyseOvenRecipe.Serializer::new
    ).register();

    public static final IERecipeEntry<SimpleCraftingRecipeSerializer<LandmineDisguiseRecipe>, LandmineDisguiseRecipe> LANDMINE_DISGUISE = REGISTRATE.recipeType(
            "landmine_disguise",
            LandmineDisguiseRecipe.class,
            () -> new SimpleCraftingRecipeSerializer<>(LandmineDisguiseRecipe::new)
    ).register();



    public static void init() {


    }

}
