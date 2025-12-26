package com.chen1335.immersiveMechanical.data;

import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.data.tag.IMDamageTypeTagsProvider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Map;
import java.util.Set;

@EventBusSubscriber(modid = ImmersiveMechanical.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataMain {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();

        DatapackBuiltinEntriesProvider builtinEntriesProvider = generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(
                generator.getPackOutput(),
                event.getLookupProvider(),
                new RegistrySetBuilder()
                        .add(Registries.DAMAGE_TYPE, IMDamageTypeProvider::bootstrap)
                ,
                Map.of(),
                Set.of(ImmersiveMechanical.MODID)
        ));

        IMMultiblockStates multiblockStates = generator.addProvider(event.includeServer(), new IMMultiblockStates(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new IMBlockStateProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new IMSimpleItemModelProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new IMBlockTagsProvider(generator.getPackOutput(), builtinEntriesProvider.getRegistryProvider(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new IMConnectorBlockStates(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new IMLootTableProvider(generator.getPackOutput(), builtinEntriesProvider.getRegistryProvider()));
        generator.addProvider(event.includeServer(), new IMItemModelProvider(generator.getPackOutput(), event.getExistingFileHelper(), multiblockStates));
        generator.addProvider(event.includeServer(), new IMDynamicModels(multiblockStates, generator.getPackOutput(), event.getExistingFileHelper()));

        generator.addProvider(event.includeServer(), new IMSoundDefinitionsProvider(generator.getPackOutput(), ImmersiveMechanical.MODID, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new IMDamageTypeTagsProvider(generator.getPackOutput(), builtinEntriesProvider.getRegistryProvider(),event.getExistingFileHelper()));


    }

    public static String modid() {
        return ImmersiveMechanical.MODID;
    }
}
