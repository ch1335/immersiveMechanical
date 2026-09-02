package com.chen1335.immersiveMechanical.data;

import com.chen1335.immersiveMechanical.API.objects.IMSounds;
import com.chen1335.registrate.devData.IESoundDefinitionsProvider;
import net.neoforged.neoforge.common.data.SoundDefinition;

public class IMSoundDefinitionsProvider {
    public static void init(IESoundDefinitionsProvider provider) {
        provider.add(IMSounds.LASER_TURRET_BEAM.value(), definition().with(provider.makeSound("laser_turret_beam")));
        provider.add(IMSounds.FLY_WHEEL.value(), definition().with(provider.makeSound("flywheel")));

    }

    public static SoundDefinition definition() {
        return SoundDefinition.definition();
    }
}
