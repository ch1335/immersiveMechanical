package com.chen1335.immersiveMechanical.definitions;

import blusunrize.immersiveengineering.common.blocks.MultiblockBEType;
import blusunrize.immersiveengineering.common.blocks.metal.EnergyConnectorBlockEntity;
import com.chen1335.immersiveMechanical.common.blockEntities.LargeBatteryCoreTile;
import com.chen1335.immersiveMechanical.common.blockEntities.TurretLaserBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.chen1335.immersiveMechanical.ImmersiveMechanical.REGISTRATE;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class IMBlockEntityTypes {
    public static final BlockEntityEntry<EnergyConnectorBlockEntity> CONNECTOR_EHV = REGISTRATE.registerConnector("connector_ehv", "EHV", false, 0.6875F, 4096 * 4, IMBlocks.CONNECTOR_EHV);

    public static final BlockEntityEntry<EnergyConnectorBlockEntity> CONNECTOR_EHV_RELAY = REGISTRATE.registerConnector("connector_ehv_relay", "EHV", true, 0.6875F, 4096 * 4, IMBlocks.CONNECTOR_EHV_RELAY);

    public static final BlockEntityEntry<LargeBatteryCoreTile> LARGE_BATTERY_CORE_TILE = REGISTRATE.blockEntity("large_battery_core_tile", LargeBatteryCoreTile::new)
            .validBlock(IMBlocks.LARGE_BATTERY_CORE)
            .register();

    public static final MultiblockBEType<TurretLaserBlockEntity> TURRET_LASER = REGISTRATE.makeMultiblock(
            "turret_laser", TurretLaserBlockEntity::new, IMBlocks.TURRET_LASER
    );

}
