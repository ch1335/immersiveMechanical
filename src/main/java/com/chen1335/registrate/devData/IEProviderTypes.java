package com.chen1335.registrate.devData;

import com.tterrag.registrate.providers.ProviderType;

public interface IEProviderTypes {
    ProviderType<IEBlockStateProvider> IE_BLOCK_STATE = ProviderType.registerProvider("ie_block_state", c -> new IEBlockStateProvider(c.parent(), c.output(), c.fileHelper()));

    ProviderType<IEMultiblockStatesProvider> MULTIBLOCK_STATE = ProviderType.registerProvider("ie_multiblock_state", c -> new IEMultiblockStatesProvider(c.parent(), c.output(), c.fileHelper()));

    ProviderType<IEDynamicModelProvider> DYNAMIC_MODELS = ProviderType.registerProvider("ie_dynamic_models", c -> new IEDynamicModelProvider(c.parent(), c.output(), c.fileHelper()));

    ProviderType<IEItemModelProvider> IE_ITEM_MODEL = ProviderType.registerProvider("ie_item_model", c -> new IEItemModelProvider(c.parent(), c.output(), c.fileHelper(), c.get(MULTIBLOCK_STATE)));

    ProviderType<IESoundDefinitionsProvider> SOUND = ProviderType.registerProvider("ie_sound", c -> new IESoundDefinitionsProvider(c.parent(), c.output(), c.parent().getModid(), c.fileHelper()));

    ProviderType<IEDamageTypeTagsProvider> DAMAGE_TYPE_TAG = ProviderType.registerProvider("ie_damage_type_tag", c -> new IEDamageTypeTagsProvider(c.parent(), c.output(), c.get(ProviderType.DYNAMIC).getRegistryProvider(), c.fileHelper()));

}
