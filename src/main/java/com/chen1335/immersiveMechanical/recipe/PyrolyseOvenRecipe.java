package com.chen1335.immersiveMechanical.recipe;

import blusunrize.immersiveengineering.api.crafting.*;
import blusunrize.immersiveengineering.api.crafting.cache.CachedRecipeList;
import blusunrize.immersiveengineering.api.utils.codec.IEDualCodecs;
import blusunrize.immersiveengineering.common.register.IEFluids;
import com.chen1335.immersiveMechanical.definitions.IMMultiblocks;
import com.chen1335.immersiveMechanical.definitions.IMRecipe;
import com.google.common.base.Suppliers;
import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualCompositeMapCodecs;
import malte0811.dualcodecs.DualMapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class PyrolyseOvenRecipe extends MultiblockRecipe {
    public static final Supplier<RecipeMultiplier> MULTIPLIERS = Suppliers.memoize(() -> new RecipeMultiplier(PyrolyseOvenRecipe::timeModifier, PyrolyseOvenRecipe::energyModifier));
    private final IngredientWithSize input;
    private final List<StackWithChance> outPutsWithChance;
    private final FluidStack fluidOutput;
    public static final CachedRecipeList<PyrolyseOvenRecipe> RECIPES = new CachedRecipeList<>(IMRecipe.PYROLYSE_OVEN.getIEType());

    private static double energyModifier() {
        return 1;
    }

    private static double timeModifier() {
        return 1;
    }

    public PyrolyseOvenRecipe(IngredientWithSize input,
                              TagOutput main,
                              List<StackWithChance> outPutsWithChance,
                              FluidStack fluidOutput,
                              int baseTime,
                              int baseEnergy
    ) {
        super(main, IMRecipe.PYROLYSE_OVEN.getIEType(), baseTime, baseEnergy, MULTIPLIERS);
        this.input = input;
        this.outPutsWithChance = outPutsWithChance;
        this.fluidOutput = fluidOutput;
    }

    public static @Nullable RecipeHolder<PyrolyseOvenRecipe> findRecipe(Level level, ItemStack input, @Nullable RecipeHolder<PyrolyseOvenRecipe> hint) {
        if (hint != null && hint.value().matches(input)) {
            return hint;
        }

        for (RecipeHolder<PyrolyseOvenRecipe> recipe : RECIPES.getRecipes(level)) {
            if (recipe.value().input.testIgnoringSize(input)) {
                return recipe;
            }
        }

        return null;
    }


    public boolean matches(ItemStack stack) {
        return input.testIgnoringSize(stack);
    }

    public IngredientWithSize getInput() {
        return input;
    }

    public TagOutput getOutput() {
        return outputDummy;
    }

    public List<StackWithChance> getOutputsWithChance() {
        return outPutsWithChance;
    }

    public FluidStack getFluidOutput() {
        return fluidOutput;
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
        public static final DualMapCodec<RegistryFriendlyByteBuf, PyrolyseOvenRecipe> CODECS = DualCompositeMapCodecs.composite(
                IngredientWithSize.CODECS.fieldOf("input"), PyrolyseOvenRecipe::getInput,
                TagOutput.CODECS.fieldOf("result"), PyrolyseOvenRecipe::getOutput,
                CHANCE_LIST_CODECS.optionalFieldOf("secondary_outputs", List.of()), PyrolyseOvenRecipe::getOutputsWithChance,
                IEDualCodecs.FLUID_STACK.optionalFieldOf("fluid_output", FluidStack.EMPTY), PyrolyseOvenRecipe::getFluidOutput,
                DualCodecs.INT.optionalFieldOf("time", 200), PyrolyseOvenRecipe::getBaseTime,
                DualCodecs.INT.fieldOf("energy"), PyrolyseOvenRecipe::getBaseEnergy,
                PyrolyseOvenRecipe::new
        );

        @Override
        public ItemStack getIcon() {
            return IMMultiblocks.PYROLYSE_OVEN.getBlock().asItem().getDefaultInstance();
        }

        @Override
        protected DualMapCodec<RegistryFriendlyByteBuf, PyrolyseOvenRecipe> codecs() {
            return CODECS;
        }
    }
}
