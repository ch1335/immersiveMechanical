package com.chen1335.immersiveMechanical.kubejs;

import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.kubejs.recipe.IndustrialFurnaceSchema;
import com.chen1335.immersiveMechanical.kubejs.recipe.PyrolyseOvenSchema;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;

public class IMKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        registry.register(ImmersiveMechanical.id("industrial_furnace"), IndustrialFurnaceSchema.SCHEMA);
        registry.register(ImmersiveMechanical.id("pyrolyse_oven"), PyrolyseOvenSchema.SCHEMA);
    }
}
