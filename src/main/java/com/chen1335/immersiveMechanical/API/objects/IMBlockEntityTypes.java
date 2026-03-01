package com.chen1335.immersiveMechanical.API.objects;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.MultiblockBEType;
import blusunrize.immersiveengineering.common.blocks.metal.EnergyConnectorBlockEntity;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.blockEntities.IMEnergyConnectorBlockEntity;
import com.chen1335.immersiveMechanical.common.blockEntities.LargeBatteryCoreTile;
import com.chen1335.immersiveMechanical.common.blockEntities.TurretLaserBlockEntity;
import com.chen1335.immersiveMechanical.mixins.immersive_mechanical.EnergyConnectorBlockEntityAccessor;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
import java.util.function.Supplier;

public class IMBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCKS = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ImmersiveMechanical.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnergyConnectorBlockEntity>> CONNECTOR_EHV = register("connector_ehv", "EHV", false, 0.6875F, 4096 * 4, () -> ImmutableSet.of(IMBlocks.CONNECTOR_EHV.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnergyConnectorBlockEntity>> CONNECTOR_EHV_RELAY = register("connector_ehv_relay", "EHV", true, 0.6875F, 4096 * 4, () -> ImmutableSet.of(IMBlocks.CONNECTOR_EHV_RELAY.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LargeBatteryCoreTile>> LARGE_BATTERY_CORE_TILE = BLOCKS.register("large_battery_core_tile", () -> new BlockEntityType<>(LargeBatteryCoreTile::new, ImmutableSet.of(IMBlocks.LARGE_BATTERY_CORE.get()), null));

    public static final MultiblockBEType<TurretLaserBlockEntity> TURRET_LASER = makeMultiblock(
            "turret_laser", TurretLaserBlockEntity::new, IMBlocks.TURRET_LASER
    );

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<EnergyConnectorBlockEntity>> register(String name, String cat, boolean relay, float length, int i, Supplier<Set<Block>> availableBlock) {
        Pair<String, Boolean> pair = Pair.of(cat, relay);
        DeferredHolder<BlockEntityType<?>, BlockEntityType<EnergyConnectorBlockEntity>> register = BLOCKS.register(name, () -> new BlockEntityType<>((pos, state) -> new IMEnergyConnectorBlockEntity(cat, relay, pos, state) {
            @Override
            public int getMaxOutput() {
                return i;
            }

            @Override
            public int getMaxInput() {
                return i;
            }
        }, availableBlock.get(), null));
        EnergyConnectorBlockEntity.NAME_TO_SPEC.put(register.getId(), pair);
        EnergyConnectorBlockEntity.SPEC_TO_TYPE.put(pair, register);
        EnergyConnectorBlockEntityAccessor.IM$getLength().put(pair, length);
        return register;
    }

    private static <T extends BlockEntity & IEBlockInterfaces.IGeneralMultiblock> MultiblockBEType<T> makeMultiblock(String name, MultiblockBEType.BEWithTypeConstructor<T> make, Supplier<? extends Block> block) {
        return new MultiblockBEType<>(
                name, BLOCKS, make, block, state -> state.hasProperty(IEProperties.MULTIBLOCKSLAVE) && !state.getValue(IEProperties.MULTIBLOCKSLAVE)
        );
    }
}
