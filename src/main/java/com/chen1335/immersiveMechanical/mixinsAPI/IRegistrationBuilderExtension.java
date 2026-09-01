package com.chen1335.immersiveMechanical.mixinsAPI;

import blusunrize.immersiveengineering.api.multiblocks.blocks.MultiblockRegistrationBuilder;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockBlockEntityDummy;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockBlockEntityMaster;
import com.chen1335.immersiveMechanical.API.IMBEConstructor;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface IRegistrationBuilderExtension<State extends IMultiblockState, Self extends MultiblockRegistrationBuilder<State, Self>> {
    Self IM$customBEs(
            MultiblockRegistrationBuilder.RegistrationMethod<BlockEntityType<?>> register,
            IMBEConstructor<State, ? extends MultiblockBlockEntityMaster<State>> masterConstruct,
            IMBEConstructor<State, ? extends MultiblockBlockEntityDummy<State>> dummyConstruct
    );
}
