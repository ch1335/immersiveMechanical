package com.chen1335.immersiveMechanical.data;

import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.data.tag.IMBlockTagsProvider;
import com.chen1335.immersiveMechanical.data.tag.IMDamageTypeTagsProvider;
import com.chen1335.immersiveMechanical.data.worldgen.IMFeatureUtils;
import com.chen1335.immersiveMechanical.data.worldgen.IMPlacementUtils;
import com.chen1335.registrate.devData.IEProviderTypes;
import com.tterrag.registrate.providers.DataProviderInitializer;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Map;
import java.util.Set;

import static com.chen1335.immersiveMechanical.ImmersiveMechanical.REGISTRATE;

@EventBusSubscriber(modid = ImmersiveMechanical.MODID)
public class DataMain {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void gatherData(GatherDataEvent event) {
        REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, IMBlockTagsProvider::init);
        REGISTRATE.addDataGenerator(IEProviderTypes.IE_BLOCK_STATE, IMBlockStateProvider::init);
        REGISTRATE.addDataGenerator(IEProviderTypes.MULTIBLOCK_STATE, IMMultiblockStates::init);
        REGISTRATE.addDataGenerator(IEProviderTypes.DYNAMIC_MODELS, IMDynamicModels::init);
        REGISTRATE.addDataGenerator(IEProviderTypes.IE_ITEM_MODEL, IMItemModelProvider::init);
        REGISTRATE.addDataGenerator(IEProviderTypes.SOUND, IMSoundDefinitionsProvider::init);
        REGISTRATE.addDataGenerator(ProviderType.RECIPE, IMRecipeProvider::init);
        REGISTRATE.addDataGenerator(ProviderType.DYNAMIC, provider -> {});

        DataProviderInitializer dataGenInitializer = REGISTRATE.getDataGenInitializer();
        dataGenInitializer.add(Registries.DAMAGE_TYPE, IMDamageTypeProvider::bootstrap);
        dataGenInitializer.add(Registries.CONFIGURED_FEATURE, IMFeatureUtils::bootstrap);
        dataGenInitializer.add(Registries.PLACED_FEATURE, IMPlacementUtils::bootstrap);
        dataGenInitializer.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, IMBiomeModifier::bootstrap);
        REGISTRATE.addDataGenerator(IEProviderTypes.DAMAGE_TYPE_TAG, IMDamageTypeTagsProvider::init);
    }

    public static String modid() {
        return ImmersiveMechanical.MODID;
    }
}
