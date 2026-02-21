package com.chen1335.immersiveMechanical.API.objects;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.MultiblockBEType;
import blusunrize.immersiveengineering.common.blocks.metal.EnergyConnectorBlockEntity;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.blockEntities.IMEnergyConnectorBlockEntity;
import com.chen1335.immersiveMechanical.common.blockEntities.LargeBatteryCoreTile;
import com.chen1335.immersiveMechanical.common.blockEntities.TurretLaserBlockEntity;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public class IMBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE_DEFERRED_REGISTER = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ImmersiveMechanical.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnergyConnectorBlockEntity>> CONNECTOR_EHV = register("connector_ehv", "EHV", false, 0.6875F, 4096 * 4);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnergyConnectorBlockEntity>> CONNECTOR_EHV_RELAY = register("connector_ehv_relay", "EHV", true, 0.6875F, 4096 * 4);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LargeBatteryCoreTile>> LARGE_BATTERY_CORE_TILE = BLOCK_ENTITY_TYPE_DEFERRED_REGISTER.register("large_battery_core_tile", () -> new BlockEntityType<>(LargeBatteryCoreTile::new, ImmutableSet.of(IMBlocks.LARGE_BATTERY_CORE.get()), null));

    public static final MultiblockBEType<TurretLaserBlockEntity> TURRET_LASER = makeMultiblock(
            "turret_laser", TurretLaserBlockEntity::new, IMBlocks.TURRET_LASER
    );

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<EnergyConnectorBlockEntity>> register(String name, String cat, boolean relay, float length, int i) {
        Pair<String, Boolean> pair = Pair.of(cat, relay);
        DeferredHolder<BlockEntityType<?>, BlockEntityType<EnergyConnectorBlockEntity>> r = BLOCK_ENTITY_TYPE_DEFERRED_REGISTER.register(name, () -> {
            return new BlockEntityType<>((pos, state) -> {
                return new IMEnergyConnectorBlockEntity(cat, relay, pos, state, i, i);
            }, ImmutableSet.of(IMBlocks.CONNECTOR_EHV.get(), IMBlocks.CONNECTOR_EHV_RELAY.get()), null);
        });
        EnergyConnectorBlockEntity.NAME_TO_SPEC.put(r.getId(), pair);
        EnergyConnectorBlockEntity.SPEC_TO_TYPE.put(pair, r);

        try {
            Field field = EnergyConnectorBlockEntity.class.getDeclaredField("LENGTH");
            field.setAccessible(true);
            Object2FloatMap<Pair<String, Boolean>> lengthMap = (Object2FloatMap<Pair<String, Boolean>>) field.get(EnergyConnectorBlockEntity.class);
            lengthMap.put(pair, length);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return r;
    }

    private static <T extends BlockEntity & IEBlockInterfaces.IGeneralMultiblock>
    MultiblockBEType<T> makeMultiblock(String name, MultiblockBEType.BEWithTypeConstructor<T> make, Supplier<? extends Block> block) {
        return new MultiblockBEType<>(
                name, BLOCK_ENTITY_TYPE_DEFERRED_REGISTER, make, block, state -> state.hasProperty(IEProperties.MULTIBLOCKSLAVE) && !state.getValue(IEProperties.MULTIBLOCKSLAVE)
        );
    }
}
