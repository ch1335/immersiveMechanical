package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.industrialFurnace;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessInMachine;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.coil.CoilInfo;
import com.chen1335.immersiveMechanical.mixins.immersive_mechanical.MultiblockProcessAccessor;
import com.chen1335.immersiveMechanical.recipe.IndustrialFurnaceRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.function.TriFunction;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.BiFunction;

public class IndustrialFurnaceProcess extends MultiblockProcessInMachine<IndustrialFurnaceRecipe> {
    private final IndustrialFurnacesLogic.State state;
    private RecipeHolder<IndustrialFurnaceRecipe> recipe;
    private CoilInfo coil;
    private LevelDependentData<IndustrialFurnaceRecipe> levelData;

    public static final TriFunction<MultiblockRecipe, Integer, Integer, LevelDependentData<IndustrialFurnaceRecipe>> DATA_CREATOR;

    public IndustrialFurnaceProcess(RecipeHolder<IndustrialFurnaceRecipe> recipe, IndustrialFurnacesLogic.State state, int... inputSlots) {
        super(recipe, inputSlots);
        this.state = state;
        this.recipe = recipe;
        IndustrialFurnaceRecipe value = recipe.value();
        int maxTicks = value.getTotalProcessTime();
        int energyPerTick = value.getTotalProcessEnergy() / maxTicks;
        levelData = DATA_CREATOR.apply(value, maxTicks, energyPerTick);
    }

    public IndustrialFurnaceProcess(BiFunction<Level, ResourceLocation, IndustrialFurnaceRecipe> getRecipe, IndustrialFurnacesLogic.State state, CompoundTag data) {
        super(getRecipe, data);
        this.state = state;
    }


    @Override
    protected LevelDependentData<IndustrialFurnaceRecipe> getLevelData(Level level) {
        if (coil != state.coilInfo || levelData == null) {
            coil = state.coilInfo;
            if (coil == null) {
                levelData = DATA_CREATOR.apply(null, 0, 0);
                return levelData;
            }

            MultiblockRecipe recipe1;
            if (recipe != null) {
                recipe1 = recipe.value();
            } else {
                MultiblockProcessAccessor accessor = (MultiblockProcessAccessor) this;
                recipe1 = accessor.im$GetRecipe().apply(level, accessor.im$RecipeId());
            }
            int maxTicks = (int) (recipe1.getTotalProcessTime() / coil.timeModify());
            int energyPerTick = (int) (((float) recipe1.getTotalProcessEnergy() / maxTicks) / coil.energyModify());
            levelData = DATA_CREATOR.apply(recipe1, maxTicks, energyPerTick);
        }
        return levelData;
    }

    static {
        Class<LevelDependentData> dependentDataClass = LevelDependentData.class;

        try {
            Constructor<LevelDependentData> constructor = dependentDataClass.getDeclaredConstructor(MultiblockRecipe.class, int.class, int.class);
            constructor.setAccessible(true);

            DATA_CREATOR = ((recipe, maxTicks, energyPerTick) -> {
                try {
                    return constructor.newInstance(recipe, maxTicks, energyPerTick);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }
}
