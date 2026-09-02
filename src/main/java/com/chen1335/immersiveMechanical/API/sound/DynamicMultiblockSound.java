package com.chen1335.immersiveMechanical.API.sound;

import blusunrize.immersiveengineering.common.util.sound.MultiblockSound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class DynamicMultiblockSound extends MultiblockSound {
    private final Supplier<Float> floatSupplier;
    private final BooleanSupplier valid;
    public DynamicMultiblockSound(BooleanSupplier active,
                                  BooleanSupplier valid,
                                  Vec3 pos,
                                  SoundEvent sound,
                                  boolean loop,
                                  Supplier<Float> floatSupplier,
                                  float maxVolume
    ) {
        super(active, valid, pos, sound, loop, maxVolume);
        this.floatSupplier = floatSupplier;
        this.valid = valid;
    }


    public static BooleanSupplier startSound(
            BooleanSupplier active, BooleanSupplier valid, Vec3 pos, Holder<SoundEvent> sound, Supplier<Float> function, float maxVolume
    ) {
        return startSound(active, valid, pos, sound, true, function, maxVolume);
    }

    public static BooleanSupplier startSound(
            BooleanSupplier active, BooleanSupplier valid, Vec3 pos, Holder<SoundEvent> sound, boolean loop, Supplier<Float> function, float maxVolume
    ) {
        final DynamicMultiblockSound instance = new DynamicMultiblockSound(active, valid, pos, sound.value(), loop, function, maxVolume);
        final SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        soundManager.play(instance);
        return () -> soundManager.isActive(instance);
    }

    @Override
    public void tick() {
        if(!valid.getAsBoolean())
            this.stop();
        volume = floatSupplier.get();
    }
}
