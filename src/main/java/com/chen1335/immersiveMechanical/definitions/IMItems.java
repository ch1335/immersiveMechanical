package com.chen1335.immersiveMechanical.definitions;

import blusunrize.immersiveengineering.common.items.IEBaseItem;
import blusunrize.immersiveengineering.common.items.WireCoilItem;
import com.chen1335.immersiveMechanical.API.objects.metal.IMMetals;
import com.chen1335.immersiveMechanical.common.wires.IMWireTypes;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;

import static com.chen1335.immersiveMechanical.ImmersiveMechanical.REGISTRATE;

public class IMItems {

    public static final ItemEntry<WireCoilItem> EHV_WIRE_COIL = REGISTRATE.item("ehv_wire_coil", properties -> new WireCoilItem(IMWireTypes.EHV))
            .defaultModel()
            .register();

    public static final ItemEntry<IEBaseItem> ACSR = simple("aluminum_conductor_steel_reinforced");

    public static final ItemEntry<Item> ROW_CHROME = REGISTRATE.item("raw_chrome", Item::new)
            .defaultModel()
            .register();

    public static final ItemEntry<Item> WIRE_NICHROME = REGISTRATE.item("wire_nichrome", Item::new)
            .defaultModel()
            .register();

    public static final ItemEntry<Item> NICHROME_WIRE_COIL = REGISTRATE.item("nichrome_wire_coil", Item::new)
            .defaultModel()
            .register();

    public static void register(IEventBus modEventBus) {
        IMMetals.register(modEventBus);
    }

    public static ItemEntry<IEBaseItem> simple(String name) {
        return REGISTRATE.item(name, IEBaseItem::new)
                .defaultModel()
                .register();
    }

    public static void init() {

    }
}
