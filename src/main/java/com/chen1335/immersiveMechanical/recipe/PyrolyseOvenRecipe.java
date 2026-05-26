package com.chen1335.immersiveMechanical.recipe;

import blusunrize.immersiveengineering.api.crafting.*;
import blusunrize.immersiveengineering.common.register.IEFluids;
import com.chen1335.immersiveMechanical.definitions.IMRecipe;
import com.google.common.base.Suppliers;
import malte0811.dualcodecs.DualMapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.function.Supplier;

public class PyrolyseOvenRecipe extends MultiblockRecipe {
    public static final Supplier<RecipeMultiplier> MULTIPLIERS = Suppliers.memoize(() -> new RecipeMultiplier(PyrolyseOvenRecipe::timeModifier, PyrolyseOvenRecipe::energyModifier));
    private final IngredientWithSize input;
    private final List<StackWithChance> outPutsWithChance;
    private final FluidStack fluidOutput;

    private static double energyModifier() {
        return 1;
    }

    private static double timeModifier() {
        return 1;
    }

    protected <T extends Recipe<?>> PyrolyseOvenRecipe(IngredientWithSize input,
                                                       TagOutput main,
                                                       List<StackWithChance> outPutsWithChance,
                                                       FluidStack fluidOutput,
                                                       int baseTime, int baseEnergy
    ) {
        super(main, IMRecipe.PYROLYSE_OVEN.getIEType(), baseTime, baseEnergy, MULTIPLIERS);
        this.input = input;
        this.outPutsWithChance = outPutsWithChance;
        this.fluidOutput = fluidOutput;
    }

    @Override
    protected IERecipeSerializer<?> getIESerializer() {
        return IMRecipe.PYROLYSE_OVEN.getSerializer();
    }

    @Override
    public int getMultipleProcessTicks() {
        return 0;
    }

    public static PyrolyseOvenRecipe fromCokeOvenRecipe(CokeOvenRecipe recipe) {
        return new PyrolyseOvenRecipe(recipe.input, recipe.output, List.of(), new FluidStack(IEFluids.CREOSOTE.getStill(), recipe.creosoteOutput), recipe.time / 4, 25600);
    }

    public static class Serializer extends IERecipeSerializer<PyrolyseOvenRecipe> implements RecipeSerializer<PyrolyseOvenRecipe> {

        @Override
        public ItemStack getIcon() {
            return null;
        }

        @Override
        protected DualMapCodec<RegistryFriendlyByteBuf, PyrolyseOvenRecipe> codecs() {
            return null;
        }
    }
}
