package com.chen1335.immersiveMechanical.data.tag;

import com.chen1335.immersiveMechanical.API.tags.IMItemTags;
import com.tterrag.registrate.providers.RegistrateItemTagsProvider;

public class IMItemTagsProvider {
    public static void init(RegistrateItemTagsProvider provider) {
        provider.addTag(IMItemTags.ORES_CHROME)
                .addTag(IMItemTags.DEEPSLATE_CHROME);
    }
}
