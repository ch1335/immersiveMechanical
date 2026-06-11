package com.chen1335.immersiveMechanical.common.blocks.multiblocks.process;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockLevel;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessInMachine;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.ProcessContext;
import com.chen1335.immersiveMechanical.mixinsAPI.ILevelDependentDataExtension;
import com.chen1335.immersiveMechanical.mixinsAPI.IMultiblockProcessExtension;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import java.util.function.BiFunction;

public class IMMultiblockProcessInMachine<R extends MultiblockRecipe> extends MultiblockProcessInMachine<R> implements IMultiblockProcessExtension<R> {
    private boolean isFirstTick = true;
    public IMMultiblockProcessInMachine(ResourceLocation recipeId, BiFunction<Level, ResourceLocation, R> getRecipe, int... inputSlots) {
        super(recipeId, getRecipe, inputSlots);
    }

    public IMMultiblockProcessInMachine(RecipeHolder<R> recipe, int... inputSlots)
    {
        super(recipe, inputSlots);
    }

    public IMMultiblockProcessInMachine(BiFunction<Level, ResourceLocation, R> getRecipe, CompoundTag data)
    {
        super(getRecipe, data);
    }

    @Override
    public void doProcessTick(ProcessContext.ProcessContextInMachine<R> context, IMultiblockLevel level) {
        if (isFirstTick) {
            isFirstTick = false;
            onFirstTick(context, level);
        }
        super.doProcessTick(context, level);
    }

    public void onFirstTick(ProcessContext.ProcessContextInMachine<R> context, IMultiblockLevel level){

    }

    public void setMaxTicks(Level level, int maxTicks){
        ILevelDependentDataExtension.class.cast(getLevelData(level)).IM$setMaxTicks(maxTicks);
    }

    public void setEnergyPerTick(Level level,int energyPerTick){
        ILevelDependentDataExtension.class.cast(getLevelData(level)).IM$setEnergyPerTick(energyPerTick);
    }
}
