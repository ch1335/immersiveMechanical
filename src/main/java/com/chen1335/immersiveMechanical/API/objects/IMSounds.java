package com.chen1335.immersiveMechanical.API.objects;

import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IMSounds {
    public static final DeferredRegister<SoundEvent> REGISTER = DeferredRegister.create(
            Registries.SOUND_EVENT, ImmersiveMechanical.MODID
    );

    public static final Holder<SoundEvent> LASER_TURRET_BEAM = registerSound("laser_turret_beam");

    private static Holder<SoundEvent> registerSound(String name) {
        return REGISTER.register(name, () -> SoundEvent.createVariableRangeEvent(ImmersiveMechanical.id(name)));
    }
}
