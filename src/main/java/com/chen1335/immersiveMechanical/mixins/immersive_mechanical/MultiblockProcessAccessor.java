package com.chen1335.immersiveMechanical.mixins.immersive_mechanical;


import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.BiFunction;

@Mixin(MultiblockProcess.class)
public interface MultiblockProcessAccessor {
    @Accessor("getRecipe")
    BiFunction<Level, ResourceLocation, MultiblockRecipe> im$GetRecipe();

    @Accessor("recipeId")
    ResourceLocation im$RecipeId();
}
