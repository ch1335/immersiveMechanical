package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.industrialFurnace;

import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockLevel;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.ProcessContext;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.process.IMMultiblockProcessInMachine;
import com.chen1335.immersiveMechanical.recipe.IndustrialFurnaceRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import java.util.function.BiFunction;

public class IndustrialFurnaceProcess extends IMMultiblockProcessInMachine<IndustrialFurnaceRecipe> {
    private final IndustrialFurnacesLogic.State state;

    public IndustrialFurnaceProcess(RecipeHolder<IndustrialFurnaceRecipe> recipe, IndustrialFurnacesLogic.State state, int... inputSlots) {
        super(recipe, inputSlots);
        this.state = state;
    }

    public IndustrialFurnaceProcess(BiFunction<Level, ResourceLocation, IndustrialFurnaceRecipe> getRecipe, IndustrialFurnacesLogic.State state, CompoundTag data) {
        super(getRecipe, data);
        this.state = state;
    }

    @Override
    public void onFirstTick(ProcessContext.ProcessContextInMachine<IndustrialFurnaceRecipe> context, IMultiblockLevel level) {
        super.onFirstTick(context, level);
        Level rawLevel = level.getRawLevel();
        LevelDependentData<IndustrialFurnaceRecipe> levelData = getLevelData(rawLevel);
        setEnergyPerTick(rawLevel, (int) (levelData.energyPerTick() * state.coilInfo.energyModify()));
        setMaxTicks(rawLevel, (int) (levelData.maxTicks() / state.coilInfo.timeModify()));
    }

}
