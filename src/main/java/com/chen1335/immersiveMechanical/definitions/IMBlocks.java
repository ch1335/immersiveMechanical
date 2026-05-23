package com.chen1335.immersiveMechanical.definitions;

import blusunrize.immersiveengineering.common.blocks.generic.ConnectorBlock;
import blusunrize.immersiveengineering.common.blocks.metal.BasicConnectorBlock;
import blusunrize.immersiveengineering.common.blocks.metal.EnergyConnectorBlockEntity;
import blusunrize.immersiveengineering.common.blocks.metal.TurretBlock;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import com.chen1335.immersiveMechanical.API.objects.IMBlockEntityTypes;
import com.chen1335.immersiveMechanical.API.registrate.data.IMLootHelper;
import com.chen1335.immersiveMechanical.common.blockEntities.TurretLaserBlockEntity;
import com.chen1335.immersiveMechanical.common.blocks.LargeBatteryBlock;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

import static com.chen1335.immersiveMechanical.ImmersiveMechanical.REGISTRATE;


public class IMBlocks {
    private static final Supplier<BlockBehaviour.Properties> METAL_PROPERTIES_DYNAMIC = () -> IEBlocks.METAL_PROPERTIES_NO_OCCLUSION.get().dynamicShape();


    public static final BlockEntry<BasicConnectorBlock<EnergyConnectorBlockEntity>> CONNECTOR_EHV = REGISTRATE.block("connector_ehv", (properties) -> new BasicConnectorBlock<>(ConnectorBlock.PROPERTIES.get(), IMBlockEntityTypes.CONNECTOR_EHV))
            .blockstate(NonNullBiConsumer.noop())
            .loot(RegistrateBlockLootTables::dropSelf)
            .item()
            .model(NonNullBiConsumer.noop())
            .build()
            .register();

    public static final BlockEntry<BasicConnectorBlock<EnergyConnectorBlockEntity>> CONNECTOR_EHV_RELAY = REGISTRATE.block("connector_ehv_relay", (properties) -> new BasicConnectorBlock<>(ConnectorBlock.PROPERTIES.get(), IMBlockEntityTypes.CONNECTOR_EHV_RELAY))
            .blockstate(NonNullBiConsumer.noop())
            .loot(RegistrateBlockLootTables::dropSelf)
            .item()
            .model(NonNullBiConsumer.noop())
            .build()
            .register();

    public static final BlockEntry<LargeBatteryBlock> LARGE_BATTERY_CORE = REGISTRATE.block("large_battery_core", (properties) -> new LargeBatteryBlock(properties
                    .strength(5.0F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .mapColor(MapColor.METAL)
            )).simpleItem()
            .defaultBlockstate()
            .loot(IMLootHelper::dropTile)
            .register();


    public static final BlockEntry<TurretBlock<TurretLaserBlockEntity>> TURRET_LASER = REGISTRATE.block("turret_laser", (properties) -> new TurretBlock<>(IMBlockEntityTypes.TURRET_LASER, METAL_PROPERTIES_DYNAMIC.get()))
            .blockstate(NonNullBiConsumer.noop())
            .loot(RegistrateBlockLootTables::dropSelf)
            .item()
            .model(NonNullBiConsumer.noop())
            .build()
            .register();

    public static final BlockEntry<DropExperienceBlock> CHROME_ORE = REGISTRATE.block("chrome_ore", (properties) -> new DropExperienceBlock(
                    ConstantInt.of(0),
                    properties
                            .mapColor(MapColor.STONE)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 3.0F)
                            .sound(SoundType.STONE)
            ))
            .simpleItem()
            .defaultBlockstate()
            .loot((lootTables, dropExperienceBlock) -> {
                lootTables.add(dropExperienceBlock, IMLootHelper.dropOre(lootTables, dropExperienceBlock, IMItems.ROW_CHROME.asItem()));
            })
            .register();

    public static final BlockEntry<DropExperienceBlock> DEEPSLATE_CHROME_ORE = REGISTRATE.block("deepslate_chrome_ore", (properties) -> new DropExperienceBlock(
                    ConstantInt.of(0),
                    BlockBehaviour.Properties.ofFullCopy(CHROME_ORE.get())
                            .mapColor(MapColor.DEEPSLATE)
                            .strength(4.5F, 3.0F)
                            .sound(SoundType.DEEPSLATE)
            ))
            .simpleItem()
            .defaultBlockstate()
            .loot((lootTables, dropExperienceBlock) -> {
                lootTables.add(dropExperienceBlock, IMLootHelper.dropOre(lootTables, dropExperienceBlock, IMItems.ROW_CHROME.asItem()));
            })
            .register();

    public static final BlockEntry<Block> COIL_NICHROME = REGISTRATE.block("coil_nichrome", (properties) -> new Block(properties
                    .mapColor(MapColor.METAL)
                    .strength(5.0F, 6.0F)
                    .requiresCorrectToolForDrops()
            ))
            .blockstate(NonNullBiConsumer.noop())
            .item()
            .model(NonNullBiConsumer.noop())
            .build()
            .loot(RegistrateBlockLootTables::dropSelf)
            .register();

    public static void init() {

    }
}
