package com.chen1335.immersiveMechanical.mixins.immersive_mechanical.client;

import blusunrize.immersiveengineering.api.client.ieobj.IEOBJCallback;
import blusunrize.immersiveengineering.client.models.obj.GeneralIEOBJModel;
import com.chen1335.immersiveMechanical.API.client.IMOBJCallback;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(GeneralIEOBJModel.class)
public abstract class GeneralIEOBJModelMixin<T> {
    @Shadow
    @Final
    private ModelProperty<T> keyProperty;

    @Shadow
    @Final
    private IEOBJCallback<T> callback;


    @Unique
    private final LoadingCache<T, Optional<TextureAtlasSprite>> IM$particleCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .build(CacheLoader.from(key -> {
                if (callback instanceof IMOBJCallback<T> imobjCallback) {
                    return Optional.ofNullable(imobjCallback.getParticleReplacement(key));
                } else {
                    return Optional.empty();
                }
            }));

    @ModifyReturnValue(method = "getParticleIcon(Lnet/neoforged/neoforge/client/model/data/ModelData;)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", at = @At("RETURN"))
    private TextureAtlasSprite wrapParticle(TextureAtlasSprite original, @Local(argsOnly = true) ModelData modelData) {
        T key = modelData.get(keyProperty);
        if (key == null) {
            return original;
        }
        return IM$particleCache.getUnchecked(key).orElse(original);
    }
}
