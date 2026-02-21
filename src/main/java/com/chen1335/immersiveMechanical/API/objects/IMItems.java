package com.chen1335.immersiveMechanical.API.objects;

import blusunrize.immersiveengineering.common.blocks.BlockItemIE;
import blusunrize.immersiveengineering.common.items.IEBaseItem;
import blusunrize.immersiveengineering.common.items.WireCoilItem;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.items.LargeBatteryBlockItem;
import com.chen1335.immersiveMechanical.common.wires.IMWireTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IMItems {
    public static final DeferredRegister<Item> ITEM_DEFERRED_REGISTER = DeferredRegister.createItems(ImmersiveMechanical.MODID);

    public static final DeferredHolder<Item, WireCoilItem> EHV_WIRE_COIL = ITEM_DEFERRED_REGISTER.register("ehv_wire_coil", () -> new WireCoilItem(IMWireTypes.EHV));

//    public static final DeferredHolder<Item, WireCoilItem> UHV_WIRE_COIL = ITEM_DEFERRED_REGISTER.register("uhv_wire_coil", () -> new WireCoilItem(IMWireTypes.UHV));

    public static final DeferredHolder<Item, BlockItem> CONNECTOR_EHV = ITEM_DEFERRED_REGISTER.register("connector_ehv", () -> new BlockItemIE(IMBlocks.CONNECTOR_EHV.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> CONNECTOR_EHV_RELAY = ITEM_DEFERRED_REGISTER.register("connector_ehv_relay", () -> new BlockItemIE(IMBlocks.CONNECTOR_EHV_RELAY.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> LARGE_BATTERY_CORE = ITEM_DEFERRED_REGISTER.register("large_battery_core", () -> new LargeBatteryBlockItem(IMBlocks.LARGE_BATTERY_CORE.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> LASER_TURRET = ITEM_DEFERRED_REGISTER.register("laser_turret", () -> new BlockItemIE(IMBlocks.TURRET_LASER.get(), new Item.Properties()));

    public static final DeferredHolder<Item, Item> ACSR = simple("aluminum_conductor_steel_reinforced");

    public static void register(IEventBus modEventBus) {
        ITEM_DEFERRED_REGISTER.register(modEventBus);
    }

    public static DeferredHolder<Item, Item> simple(String name) {
        return ITEM_DEFERRED_REGISTER.register(name, () -> new IEBaseItem());
    }
}
