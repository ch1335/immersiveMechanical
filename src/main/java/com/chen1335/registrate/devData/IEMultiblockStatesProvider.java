package com.chen1335.registrate.devData;

import blusunrize.immersiveengineering.data.blockstates.MultiblockStates;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.RegistrateProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class IEMultiblockStatesProvider extends MultiblockStates implements RegistrateProvider {
    private final AbstractRegistrate<?> parent;

    public IEMultiblockStatesProvider(AbstractRegistrate<?> parent, PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, exFileHelper);
        this.parent = parent;
    }

    @Override
    protected void registerStatesAndModels() {
        parent.genData(IEProviderTypes.MULTIBLOCK_STATE, this);
    }


    @Override
    public @NotNull LogicalSide getSide() {
        return LogicalSide.CLIENT;
    }
}
