package com.chen1335.registrate.devData;

import blusunrize.immersiveengineering.data.blockstates.BlockStates;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.RegistrateProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class IEBlockStateProvider extends BlockStates implements RegistrateProvider {
    private final AbstractRegistrate<?> parent;

    public IEBlockStateProvider(AbstractRegistrate<?> parent, PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, existingFileHelper);
        this.parent = parent;
    }


    @Override
    protected void registerStatesAndModels() {
        parent.genData(IEProviderTypes.IE_BLOCK_STATE, this);
    }


    @Override
    public @NotNull LogicalSide getSide() {
        return LogicalSide.CLIENT;
    }
}
