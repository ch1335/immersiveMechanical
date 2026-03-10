package com.chen1335.immersiveMechanical.mixins.immersive_mechanical.client;

import com.chen1335.immersiveMechanical.client.test.TEST;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;bindWrite(Z)V"))
    private void onRender(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        TEST.RenderEffect(deltaTracker);
    }
}
