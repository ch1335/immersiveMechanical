package com.chen1335.immersiveMechanical.recipe;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.IERecipeTypes;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import net.minecraft.world.item.crafting.Recipe;

import java.util.function.Supplier;

public class PyrolyseOvenRecipe extends MultiblockRecipe {
    protected <T extends Recipe<?>> PyrolyseOvenRecipe(TagOutput outputDummy, IERecipeTypes.TypeWithClass<T> type, int baseTime, int baseEnergy, Supplier<RecipeMultiplier> multipliers) {
        super(outputDummy, type, baseTime, baseEnergy, multipliers);
    }

    @Override
    protected IERecipeSerializer<?> getIESerializer() {
        return null;
    }

    @Override
    public int getMultipleProcessTicks() {
        return 0;
    }
}
