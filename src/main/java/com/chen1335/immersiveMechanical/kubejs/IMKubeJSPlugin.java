package com.chen1335.immersiveMechanical.kubejs;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import net.neoforged.fml.ModList;

public class IMKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        if (ModList.get().isLoaded("immersive_engineering_js")) {
            IEJSKubeJSPlugin.INSTANCE.registerRecipeSchemas(registry);
        }
    }
}
