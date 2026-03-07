package com.chen1335.immersiveMechanical.API.objects;

import blusunrize.immersiveengineering.common.blocks.generic.ConnectorBlock;
import blusunrize.immersiveengineering.common.blocks.metal.BasicConnectorBlock;
import blusunrize.immersiveengineering.common.blocks.metal.EnergyConnectorBlockEntity;
import blusunrize.immersiveengineering.common.blocks.metal.TurretBlock;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.blockEntities.TurretLaserBlockEntity;
import com.chen1335.immersiveMechanical.common.blocks.LargeBatteryBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class IMBlocks {
    private static final Supplier<BlockBehaviour.Properties> METAL_PROPERTIES_DYNAMIC = () -> IEBlocks.METAL_PROPERTIES_NO_OCCLUSION.get().dynamicShape();


    public static final DeferredRegister.Blocks BLOCK_DEFERRED_REGISTER = DeferredRegister.createBlocks(ImmersiveMechanical.MODID);

    public static final DeferredBlock<BasicConnectorBlock<EnergyConnectorBlockEntity>> CONNECTOR_EHV = BLOCK_DEFERRED_REGISTER.register("connector_ehv", () -> new BasicConnectorBlock<>(ConnectorBlock.PROPERTIES.get(), IMBlockEntityTypes.CONNECTOR_EHV));

    public static final DeferredBlock<BasicConnectorBlock<EnergyConnectorBlockEntity>> CONNECTOR_EHV_RELAY = BLOCK_DEFERRED_REGISTER.register("connector_ehv_relay", () -> new BasicConnectorBlock<>(ConnectorBlock.PROPERTIES.get(), IMBlockEntityTypes.CONNECTOR_EHV_RELAY));
    public static final DeferredBlock<Block> LARGE_BATTERY_CORE = BLOCK_DEFERRED_REGISTER.register("large_battery_core", () -> new LargeBatteryBlock(BlockBehaviour.Properties.of()
            .strength(5.0F, 6.0F)
            .requiresCorrectToolForDrops()
            .mapColor(MapColor.METAL)
    ));

    public static final DeferredBlock<TurretBlock<TurretLaserBlockEntity>> TURRET_LASER = BLOCK_DEFERRED_REGISTER.register("turret_laser", () -> new TurretBlock<>(IMBlockEntityTypes.TURRET_LASER, METAL_PROPERTIES_DYNAMIC.get()));

    public static final DeferredBlock<Block> CHROME_ORE = BLOCK_DEFERRED_REGISTER.register("chrome_ore", () -> new Block(BlockBehaviour.Properties.of()));

    public static final DeferredBlock<Block> DEEPSLATE_CHROME_ORE = BLOCK_DEFERRED_REGISTER.register("deepslate_chrome_ore", () -> new Block(BlockBehaviour.Properties.of()));

}
