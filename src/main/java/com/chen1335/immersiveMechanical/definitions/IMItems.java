package com.chen1335.immersiveMechanical.definitions;

import blusunrize.immersiveengineering.common.items.IEBaseItem;
import blusunrize.immersiveengineering.common.items.WireCoilItem;
import com.chen1335.immersiveMechanical.API.tags.IMItemTags;
import com.chen1335.immersiveMechanical.common.items.misc.LandmineItem;
import com.chen1335.immersiveMechanical.common.wires.IMWireTypes;
import com.chen1335.registrate.IERegistrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;

import static com.chen1335.immersiveMechanical.ImmersiveMechanical.REGISTRATE;

public class IMItems {

    public static final ItemEntry<WireCoilItem> EHV_WIRE_COIL = REGISTRATE.item("ehv_wire_coil", properties -> new WireCoilItem(IMWireTypes.EHV))
            .register();

    public static final ItemEntry<IEBaseItem> ACSR = simple("aluminum_conductor_steel_reinforced")
            .register();

    public static final ItemEntry<Item> ROW_CHROME = REGISTRATE.item("raw_chrome", Item::new)
            .tag(IMItemTags.RAW_CHROME)
            .register();

    public static final ItemEntry<Item> WIRE_NICHROME = REGISTRATE.item("wire_nichrome", Item::new)
            .register();

    public static final ItemEntry<Item> NICHROME_WIRE_COIL = REGISTRATE.item("nichrome_wire_coil", Item::new)
            .register();

    public static final ItemEntry<LandmineItem> LANDMINE = REGISTRATE.item("landmine", LandmineItem::new)
            .model((c,p)->{})
            .register();

    public static void register(IEventBus modEventBus) {

    }

    public static ItemBuilder<IEBaseItem, IERegistrate> simple(String name) {
        return REGISTRATE.item(name, IEBaseItem::new)
                .defaultModel();
    }

    public static void init() {

    }
}
