package com.chen1335.immersiveMechanical.API.objects;

import blusunrize.immersiveengineering.common.blocks.generic.ConnectorBlock;
import blusunrize.immersiveengineering.common.blocks.metal.BasicConnectorBlock;
import blusunrize.immersiveengineering.common.blocks.metal.EnergyConnectorBlockEntity;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.blocks.LargeBatteryBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IMBlocks {
    public static final DeferredRegister<Block> BLOCK_DEFERRED_REGISTER = DeferredRegister.createBlocks(ImmersiveMechanical.MODID);

    public static final DeferredHolder<Block, BasicConnectorBlock<EnergyConnectorBlockEntity>> CONNECTOR_EHV = BLOCK_DEFERRED_REGISTER.register("connector_ehv", () -> new BasicConnectorBlock<>(ConnectorBlock.PROPERTIES.get(), IMBlockEntityTypes.CONNECTOR_EHV));

    public static final DeferredHolder<Block, BasicConnectorBlock<EnergyConnectorBlockEntity>> CONNECTOR_EHV_RELAY = BLOCK_DEFERRED_REGISTER.register("connector_ehv_relay", () -> new BasicConnectorBlock<>(ConnectorBlock.PROPERTIES.get(), IMBlockEntityTypes.CONNECTOR_EHV_RELAY));
    public static final DeferredHolder<Block, Block> LARGE_BATTERY_CORE_BLOCK = BLOCK_DEFERRED_REGISTER.register("large_battery_core_block", () -> new LargeBatteryBlock(BlockBehaviour.Properties.of()));


}
