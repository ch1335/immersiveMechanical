package com.chen1335.immersiveMechanical.API.client;

import blusunrize.immersiveengineering.api.client.ieobj.IEOBJCallback;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import javax.annotation.Nullable;

public interface IMOBJCallback<Key> extends IEOBJCallback<Key> {
    @Nullable
    default TextureAtlasSprite getParticleReplacement(Key object) {
        return null;
    }
}
