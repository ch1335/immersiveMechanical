package com.chen1335.immersiveMechanical.definitions;

import blusunrize.immersiveengineering.common.blocks.BlockItemIE;
import blusunrize.immersiveengineering.common.items.IEBaseItem;
import blusunrize.immersiveengineering.common.items.WireCoilItem;
import com.chen1335.immersiveMechanical.API.objects.IMBlocks;
import com.chen1335.immersiveMechanical.API.objects.metal.IMMetals;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.items.LargeBatteryBlockItem;
import com.chen1335.immersiveMechanical.common.wires.IMWireTypes;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.chen1335.immersiveMechanical.ImmersiveMechanical.REGISTRATE;

public class IMItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ImmersiveMechanical.MODID);


    public static final DeferredItem<WireCoilItem> EHV_WIRE_COIL = ITEMS.register("ehv_wire_coil", () -> new WireCoilItem(IMWireTypes.EHV));

//    public static final DeferredHolder<Item, WireCoilItem> UHV_WIRE_COIL = ITEM_DEFERRED_REGISTER.register("uhv_wire_coil", () -> new WireCoilItem(IMWireTypes.UHV));

    public static final DeferredItem<BlockItem> CONNECTOR_EHV = ITEMS.register("connector_ehv", () -> new BlockItemIE(IMBlocks.CONNECTOR_EHV.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> CONNECTOR_EHV_RELAY = ITEMS.register("connector_ehv_relay", () -> new BlockItemIE(IMBlocks.CONNECTOR_EHV_RELAY.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> LARGE_BATTERY_CORE = ITEMS.register("large_battery_core", () -> new LargeBatteryBlockItem(IMBlocks.LARGE_BATTERY_CORE.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> LASER_TURRET = ITEMS.register("laser_turret", () -> new BlockItemIE(IMBlocks.TURRET_LASER.get(), new Item.Properties()));

    public static final DeferredItem<Item> ACSR = simple("aluminum_conductor_steel_reinforced");

    public static final DeferredItem<BlockItem> CHROME_ORE = ITEMS.register("chrome_ore", () -> new BlockItemIE(IMBlocks.CHROME_ORE.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> DEEPSLATE_CHROME_ORE = ITEMS.register("deepslate_chrome_ore", () -> new BlockItemIE(IMBlocks.DEEPSLATE_CHROME_ORE.get()));

    public static final DeferredItem<BlockItem> COIL_NICHROME = ITEMS.register("coil_nichrome", () -> new BlockItemIE(IMBlocks.COIL_NICHROME.get()));

    public static final ItemEntry<Item> ROW_CHROME = REGISTRATE.item("raw_chrome", Item::new).register();

    public static final ItemEntry<Item> WIRE_NICHROME = REGISTRATE.item("wire_nichrome", Item::new).register();

    public static final ItemEntry<Item> NICHROME_WIRE_COIL = REGISTRATE.item("nichrome_wire_coil", Item::new).register();

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        IMMetals.register(modEventBus);
    }


    public static DeferredItem<Item> simple(String name) {
        return ITEMS.register(name, () -> new IEBaseItem());
    }
}
