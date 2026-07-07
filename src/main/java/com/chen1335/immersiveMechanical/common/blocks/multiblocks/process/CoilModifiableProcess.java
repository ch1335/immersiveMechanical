package com.chen1335.immersiveMechanical.common.blocks.multiblocks.process;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockLevel;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.ProcessContext;
import com.chen1335.immersiveMechanical.API.ICoilModifiableMultiblockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import java.util.function.BiFunction;

public class CoilModifiableProcess<R extends MultiblockRecipe, S extends ICoilModifiableMultiblockState> extends IMMultiblockProcessInMachine<R> {
    private final S state;

    public CoilModifiableProcess(RecipeHolder<R> recipe, S state, int... inputSlots) {
        super(recipe, inputSlots);
        this.state = state;
    }

    public CoilModifiableProcess(BiFunction<Level, ResourceLocation, R> getRecipe, S state, CompoundTag data) {
        super(getRecipe, data);
        this.state = state;
    }

    @Override
    public void onFirstTick(ProcessContext.ProcessContextInMachine<R> context, IMultiblockLevel level) {
        super.onFirstTick(context, level);
        Level rawLevel = level.getRawLevel();
        LevelDependentData<R> levelData = getLevelData(rawLevel);
        setEnergyPerTick(rawLevel, (int) (levelData.energyPerTick() * state.getCoilInfo().energyModify().getAsDouble()));
        setMaxTicks(rawLevel, (int) (levelData.maxTicks() / state.getCoilInfo().timeModify().getAsDouble()));
    }

}
