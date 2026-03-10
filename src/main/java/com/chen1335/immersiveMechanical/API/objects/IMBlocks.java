package com.chen1335.immersiveMechanical.API.objects;

import blusunrize.immersiveengineering.common.blocks.generic.ConnectorBlock;
import blusunrize.immersiveengineering.common.blocks.metal.BasicConnectorBlock;
import blusunrize.immersiveengineering.common.blocks.metal.EnergyConnectorBlockEntity;
import blusunrize.immersiveengineering.common.blocks.metal.TurretBlock;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.blockEntities.TurretLaserBlockEntity;
import com.chen1335.immersiveMechanical.common.blocks.LargeBatteryBlock;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class IMBlocks {
    private static final Supplier<BlockBehaviour.Properties> METAL_PROPERTIES_DYNAMIC = () -> IEBlocks.METAL_PROPERTIES_NO_OCCLUSION.get().dynamicShape();


    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ImmersiveMechanical.MODID);

    public static final DeferredBlock<BasicConnectorBlock<EnergyConnectorBlockEntity>> CONNECTOR_EHV = BLOCKS.register("connector_ehv", () -> new BasicConnectorBlock<>(ConnectorBlock.PROPERTIES.get(), IMBlockEntityTypes.CONNECTOR_EHV));

    public static final DeferredBlock<BasicConnectorBlock<EnergyConnectorBlockEntity>> CONNECTOR_EHV_RELAY = BLOCKS.register("connector_ehv_relay", () -> new BasicConnectorBlock<>(ConnectorBlock.PROPERTIES.get(), IMBlockEntityTypes.CONNECTOR_EHV_RELAY));
    public static final DeferredBlock<Block> LARGE_BATTERY_CORE = BLOCKS.register("large_battery_core", () -> new LargeBatteryBlock(BlockBehaviour.Properties.of()
            .strength(5.0F, 6.0F)
            .requiresCorrectToolForDrops()
            .mapColor(MapColor.METAL)
    ));

    public static final DeferredBlock<TurretBlock<TurretLaserBlockEntity>> TURRET_LASER = BLOCKS.register("turret_laser", () -> new TurretBlock<>(IMBlockEntityTypes.TURRET_LASER, METAL_PROPERTIES_DYNAMIC.get()));

    public static final DeferredBlock<Block> CHROME_ORE = BLOCKS.register("chrome_ore", () -> new DropExperienceBlock(
            ConstantInt.of(0),
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F)
    ));

    public static final DeferredBlock<Block> DEEPSLATE_CHROME_ORE = BLOCKS.register("deepslate_chrome_ore", () -> new DropExperienceBlock(
            ConstantInt.of(0), BlockBehaviour.Properties.ofFullCopy(CHROME_ORE.get()).mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)
    ));

    public static final DeferredBlock<Block> COIL_NICHROME = BLOCKS.register("coil_nichrome", () -> new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .strength(5.0F, 6.0F)
            .requiresCorrectToolForDrops()
    ));
}
