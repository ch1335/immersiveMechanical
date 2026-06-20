package com.chen1335.registrate.devData;

import blusunrize.immersiveengineering.data.DynamicModels;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.RegistrateProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class IEDynamicModelProvider extends ModelProvider<DynamicModels.SimpleModelBuilder> implements RegistrateProvider {
    private final AbstractRegistrate<?> parent;
    private final IEMultiblockStatesProvider multiblockStatesProvider;

    public IEDynamicModelProvider(AbstractRegistrate<?> parent, PackOutput output, ExistingFileHelper existingFileHelper, IEMultiblockStatesProvider multiblockStatesProvider) {
        super(output, parent.getModid(), "dynamic", (rl) -> new DynamicModels.SimpleModelBuilder(rl, existingFileHelper), existingFileHelper);
        this.parent = parent;
        this.multiblockStatesProvider = multiblockStatesProvider;
    }

    @Override
    protected void registerModels() {
        parent.genData(IEProviderTypes.DYNAMIC_MODELS, this);
    }

    @Override
    public @NotNull LogicalSide getSide() {
        return LogicalSide.CLIENT;
    }


    @Override
    public @NotNull String getName() {
        return "Dynamic models";
    }

    public IEMultiblockStatesProvider getMultiblockStatesProvider() {
        return multiblockStatesProvider;
    }
}
