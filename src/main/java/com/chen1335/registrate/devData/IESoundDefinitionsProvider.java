package com.chen1335.registrate.devData;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.RegistrateProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class IESoundDefinitionsProvider extends SoundDefinitionsProvider implements RegistrateProvider {
    private final AbstractRegistrate<?> parent;

    protected IESoundDefinitionsProvider(AbstractRegistrate<?> parent, PackOutput output, String modId, ExistingFileHelper helper) {
        super(output, modId, helper);
        this.parent = parent;
    }

    @Override
    public void registerSounds() {
        parent.genData(IEProviderTypes.SOUND, this);
    }

    public SoundDefinition.Sound makeSound(String name) {
        return sound(ResourceLocation.fromNamespaceAndPath(parent.getModid(), name));
    }

    @Override
    public void add(String soundEvent, SoundDefinition definition) {
        super.add(soundEvent, definition);
    }

    @Override
    public void add(Supplier<SoundEvent> soundEvent, SoundDefinition definition) {
        super.add(soundEvent, definition);
    }

    @Override
    public void add(SoundEvent soundEvent, SoundDefinition definition) {
        super.add(soundEvent, definition);
    }

    @Override
    public void add(ResourceLocation soundEvent, SoundDefinition definition) {
        super.add(soundEvent, definition);
    }

    @Override
    public @NotNull LogicalSide getSide() {
        return LogicalSide.CLIENT;
    }
}
