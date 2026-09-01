package com.chen1335.immersiveMechanical.mixins.immersive_mechanical;

import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistration;
import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistrationBuilder;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockBlockEntityDummy;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockBlockEntityMaster;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockPartBlock;
import com.chen1335.immersiveMechanical.API.IMBEConstructor;
import com.chen1335.immersiveMechanical.mixinsAPI.IRegistrationBuilderExtension;
import com.google.common.base.Preconditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Set;
import java.util.function.Supplier;

@Mixin(MultiblockRegistrationBuilder.class)
public abstract class MultiblockRegistrationBuilderMixin<State extends IMultiblockState, Self extends MultiblockRegistrationBuilder<State, Self>> implements IRegistrationBuilderExtension<State, Self> {
    @Shadow
    private Supplier<BlockEntityType<? extends MultiblockBlockEntityMaster<State>>> masterBE;

    @Shadow
    private Supplier<BlockEntityType<? extends MultiblockBlockEntityDummy<State>>> dummyBE;

    @Shadow
    protected abstract Self self();

    @Shadow
    @Final
    private ResourceLocation name;

    @Shadow
    @Final
    public static String DUMMY_BE_SUFFIX;

    @Shadow
    @Final
    public static String MASTER_BE_SUFFIX;

    @Shadow
    private Supplier<? extends MultiblockPartBlock<State>> block;

    @Shadow
    private MultiblockRegistration<State> result;

    @Unique

    public Self IM$customBEs(
            MultiblockRegistrationBuilder.RegistrationMethod<BlockEntityType<?>> register,
            IMBEConstructor<State, ? extends MultiblockBlockEntityMaster<State>> masterConstruct,
            IMBEConstructor<State, ? extends MultiblockBlockEntityDummy<State>> dummyConstruct
    ) {
        Preconditions.checkState(this.masterBE == null);
        Preconditions.checkState(this.dummyBE == null);
        this.masterBE = register.register(name.getPath() + MASTER_BE_SUFFIX, () -> IM$makeBEType(masterConstruct));
        this.dummyBE = register.register(name.getPath() + DUMMY_BE_SUFFIX, () -> IM$makeBEType(dummyConstruct));
        return self();
    }

    @Unique
    private <BE extends BlockEntity>
    BlockEntityType<? extends BE> IM$makeBEType(
            IMBEConstructor<State, BE> construct
    ) {
        Mutable<BlockEntityType<? extends BE>> resultBox = new MutableObject<>();
        resultBox.setValue(new BlockEntityType<>(
                (pos, state) -> construct.make(resultBox.getValue(), pos, state, result),
                Set.of(block.get()),
                null
        ));
        return resultBox.getValue();
    }

}
