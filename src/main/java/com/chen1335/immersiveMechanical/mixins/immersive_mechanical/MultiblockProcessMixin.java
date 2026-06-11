package com.chen1335.immersiveMechanical.mixins.immersive_mechanical;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcess;
import com.chen1335.immersiveMechanical.mixinsAPI.IMultiblockProcessExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(MultiblockProcess.class)
public class MultiblockProcessMixin<R extends MultiblockRecipe> implements IMultiblockProcessExtension<R> {
    @ModifyArgs(method = "populateLevelData", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/common/blocks/multiblocks/process/MultiblockProcess$LevelDependentData;<init>(Lblusunrize/immersiveengineering/api/crafting/MultiblockRecipe;II)V"))
    private void modifyLevelData(Args args) {
        args.set(1, getMaxTicks(args.get(1)));
        args.set(2, getEnergyPerTick(args.get(2)));
    }
}
