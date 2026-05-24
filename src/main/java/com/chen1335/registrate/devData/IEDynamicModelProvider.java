package com.chen1335.registrate.devData;

import blusunrize.immersiveengineering.client.models.obj.callback.DynamicSubmodelCallbacks;
import blusunrize.immersiveengineering.data.DynamicModels;
import blusunrize.immersiveengineering.data.models.IEOBJBuilder;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.RegistrateProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class IEDynamicModelProvider extends ModelProvider<DynamicModels.SimpleModelBuilder> implements RegistrateProvider {
    private final AbstractRegistrate<?> parent;

    public IEDynamicModelProvider(AbstractRegistrate<?> parent, PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, parent.getModid(), "dynamic", (rl) -> new DynamicModels.SimpleModelBuilder(rl, existingFileHelper), existingFileHelper);
        this.parent = parent;
    }

    @Override
    protected void registerModels() {
        parent.genData(IEProviderTypes.DYNAMIC_MODELS, this);
    }

    @Override
    public @NotNull LogicalSide getSide() {
        return LogicalSide.CLIENT;
    }

    public void init() {
        getBuilder(ImmersiveMechanical.id("laser_turret").toString())
                .customLoader(IEOBJBuilder::begin)
                .modelLocation(ImmersiveMechanical.id("models/block/metal_device/turret_laser.obj.ie"))
                .callback(DynamicSubmodelCallbacks.INSTANCE)
                .end();
    }

    @Override
    public @NotNull String getName() {
        return "Dynamic models";
    }
}
