package com.chen1335.immersiveMechanical.recipe;

import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import blusunrize.immersiveengineering.api.crafting.TagOutputList;
import blusunrize.immersiveengineering.api.crafting.cache.CachedRecipeList;
import com.chen1335.immersiveMechanical.API.objects.IMRecipe;
import com.chen1335.immersiveMechanical.definitions.IMMultiblocks;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import malte0811.dualcodecs.DualMapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public final class IndustrialFurnaceRecipe extends MultiblockRecipe {
    public static final Supplier<RecipeMultiplier> MULTIPLIERS = Suppliers.memoize(() -> new RecipeMultiplier(IndustrialFurnaceRecipe::timeModifier, IndustrialFurnaceRecipe::energyModifier));
    public static final CachedRecipeList<IndustrialFurnaceRecipe> RECIPES = new CachedRecipeList<>(IMRecipe.Types.INDUSTRIAL_FURNACE);


    private final Ingredient input;
    private final TagOutput output;

    public IndustrialFurnaceRecipe(Ingredient input, TagOutput output, int time, int energy) {
        super(output, IMRecipe.Types.INDUSTRIAL_FURNACE, time, energy, MULTIPLIERS);
        this.input = input;
        this.output = output;
        outputList = new TagOutputList(output);
        setInputList(List.of(input));
    }


    @Nullable
    public static RecipeHolder<IndustrialFurnaceRecipe> findRecipe(Level level, ItemStack input) {
        for (RecipeHolder<IndustrialFurnaceRecipe> recipe : RECIPES.getRecipes(level)) {
            if (recipe.value().input().test(input)) {
                return recipe;
            }
        }

        return null;
    }

    public static double timeModifier() {
        return 1;
    }

    public static double energyModifier() {
        return 1;
    }

    public static IndustrialFurnaceRecipe fromSmeltingRecipe(AbstractCookingRecipe recipe, HolderLookup.Provider registries) {
        return new IndustrialFurnaceRecipe(recipe.getIngredients().getFirst(), new TagOutput(recipe.getResultItem(registries)), recipe.getCookingTime() / 2, recipe.getCookingTime() * 64);
    }

    @Override
    protected IERecipeSerializer<?> getIESerializer() {
        return IMRecipe.Serializers.INDUSTRIAL_FURNACE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return IMRecipe.Types.INDUSTRIAL_FURNACE.get();
    }

    public Ingredient input() {
        return input;
    }

    public TagOutput output() {
        return output;
    }

    @Override
    public int getMultipleProcessTicks() {
        return 0;
    }

    public static boolean isValidRecipeInput(Level level, ItemStack stack) {
        for (RecipeHolder<IndustrialFurnaceRecipe> recipe : RECIPES.getRecipes(level))
            if (recipe.value().input().test(stack))
                return true;
        return false;
    }

    public static class Serializer extends IERecipeSerializer<IndustrialFurnaceRecipe> implements RecipeSerializer<IndustrialFurnaceRecipe> {
        private static final MapCodec<IndustrialFurnaceRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("input").forGetter(IndustrialFurnaceRecipe::input),
                TagOutput.CODECS.codec().fieldOf("output").forGetter(IndustrialFurnaceRecipe::output),
                Codec.INT.fieldOf("time").forGetter(IndustrialFurnaceRecipe::getBaseTime),
                Codec.INT.fieldOf("energy").forGetter(IndustrialFurnaceRecipe::getBaseEnergy)
        ).apply(instance, IndustrialFurnaceRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, IndustrialFurnaceRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC,
                IndustrialFurnaceRecipe::input,
                TagOutput.CODECS.streamCodec(),
                IndustrialFurnaceRecipe::output,
                ByteBufCodecs.INT,
                IndustrialFurnaceRecipe::getBaseTime,
                ByteBufCodecs.INT,
                IndustrialFurnaceRecipe::getBaseEnergy,
                IndustrialFurnaceRecipe::new
        );


        @Override
        public ItemStack getIcon() {
            return IMMultiblocks.INDUSTRIAL_FURNACES.registration().iconStack();
        }

        @Override
        protected DualMapCodec<RegistryFriendlyByteBuf, IndustrialFurnaceRecipe> codecs() {
            return new DualMapCodec<>(CODEC, STREAM_CODEC);
        }
    }
}
