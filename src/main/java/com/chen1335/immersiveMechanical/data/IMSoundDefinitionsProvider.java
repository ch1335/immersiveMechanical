package com.chen1335.immersiveMechanical.data;

import com.chen1335.immersiveMechanical.API.objects.IMSounds;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class IMSoundDefinitionsProvider extends SoundDefinitionsProvider {
    protected IMSoundDefinitionsProvider(PackOutput output, String modId, ExistingFileHelper helper) {
        super(output, modId, helper);
    }

    @Override
    public void registerSounds() {
        this.add(IMSounds.LASER_TURRET_BEAM.value(), definition().with(sound("laser_turret_beam")));
    }

    protected static SoundDefinition.Sound sound(final String name) {
        return sound(ImmersiveMechanical.id(name));
    }
}
