package com.chen1335.immersiveMechanical.mixins.immersive_mechanical;

import blusunrize.immersiveengineering.api.IEApi;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IEApi.class)
public class IEApiMixin {
    @Inject(method = "ieLoc", at = @At("HEAD"), cancellable = true)
    private static void wrapLoc(String path, CallbackInfoReturnable<ResourceLocation> cir) {
        if (path.contains(":")) {
            cir.setReturnValue(ResourceLocation.parse(path));
        }
    }
}
