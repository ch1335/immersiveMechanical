package com.chen1335.immersiveMechanical.API.objects;

import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.api.utils.TagUtils;
import blusunrize.immersiveengineering.common.blocks.BlockItemIE;
import blusunrize.immersiveengineering.common.items.IEBaseItem;
import blusunrize.immersiveengineering.common.items.WireCoilItem;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.items.LargeBatteryBlockItem;
import com.chen1335.immersiveMechanical.common.wires.IMWireTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

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

    public static final DeferredItem<Item> ROW_CHROME = simple("raw_chrome");

    public enum Metals {
        CHROME("chrome"),
        NICHROME("nichrome");
        public static final Map<Metals, Map<MetalTypes, DeferredItem<Item>>> METALS = new EnumMap<>(Metals.class);

        public static final Map<Metals, Map<MetalTypes, TagKey<Item>>> METAL_TAGS = new EnumMap<>(Metals.class);
        private final String name;

        Metals(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static DeferredItem<Item> getMetal(Metals metals, MetalTypes metalTypes) {
            return IMItems.Metals.METALS.get(metals).get(metalTypes);
        }

        public static TagKey<Item> getTag(Metals metals, MetalTypes metalTypes) {
            return IMItems.Metals.METAL_TAGS.get(metals).get(metalTypes);
        }

        public static void register(IEventBus modEventBus) {
            for (Metals metals : Metals.values()) {
                for (MetalTypes metalTypes : MetalTypes.values()) {
                    Map<MetalTypes, DeferredItem<Item>> map = METALS.computeIfAbsent(metals, metals1 -> new EnumMap<>(MetalTypes.class));
                    DeferredItem<Item> simple = simple(metalTypes.getFormat().formatted(metals.name));
                    map.put(metalTypes, simple);

                    Map<MetalTypes, TagKey<Item>> tagKeyMap = METAL_TAGS.computeIfAbsent(metals, metals1 -> new EnumMap<>(MetalTypes.class));
                    tagKeyMap.put(metalTypes, metalTypes.getTag(metals.name));
                }
            }
        }

        public enum MetalTypes {
            INGOTS("ingot_%s", Tags.Items.INGOTS, IETags::getIngot),
            NUGGETS("nugget_%s", Tags.Items.NUGGETS, IETags::getNugget),
            DUSTS("dust_%s", Tags.Items.DUSTS, IETags::getDust),
            PLATES("plate_%s", IETags.plates, IETags::getPlate),
            STICK("stick_%s", Tags.Items.RODS, IETags::getRod);

            private final String format;
            private final TagKey<Item> typTag;
            private final Function<String, ResourceLocation> tagGetter;


            MetalTypes(String format, TagKey<Item> typTag, Function<String, ResourceLocation> tagGetter) {
                this.format = format;
                this.typTag = typTag;
                this.tagGetter = tagGetter;
            }

            public String getFormat() {
                return format;
            }

            public TagKey<Item> getTag(String name) {
                return TagUtils.createItemWrapper(tagGetter.apply(name));
            }

            public TagKey<Item> getTypTag() {
                return typTag;
            }
        }
    }

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        Metals.register(modEventBus);
    }


    public static DeferredItem<Item> simple(String name) {
        return ITEMS.register(name, () -> new IEBaseItem());
    }
}
