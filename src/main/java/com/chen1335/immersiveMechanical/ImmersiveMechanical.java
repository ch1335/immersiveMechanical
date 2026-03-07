package com.chen1335.immersiveMechanical;

import blusunrize.immersiveengineering.common.register.IEBlocks;
import com.chen1335.immersiveMechanical.API.objects.*;
import com.chen1335.immersiveMechanical.client.IMClient;
import com.chen1335.immersiveMechanical.common.IMStructureSource;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.IMMultiblocks;
import com.chen1335.immersiveMechanical.common.register.IMMultiblockLogic;
import com.chen1335.immersiveMechanical.common.wires.IMWireTypes;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(ImmersiveMechanical.MODID)
public class ImmersiveMechanical {
    public static final String MODID = "immersive_mechanical";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("immersive_mechanical", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.immersive_mechanical")) //The language key for the title of your CreativeModeTab
            .icon(Items.IRON_INGOT::getDefaultInstance)
            .displayItems((parameters, output) -> {
                for (DeferredHolder<Item, ? extends Item> entry : IMItems.ITEMS.getEntries()) {
                    output.accept(entry.value().getDefaultInstance());
                }
            }).build());

    public ImmersiveMechanical(IEventBus modEventBus, Dist dist, ModContainer modContainer) {
        CREATIVE_MODE_TABS.register(modEventBus);
        IMBlocks.BLOCK_DEFERRED_REGISTER.register(modEventBus);
        IMBlockEntityTypes.BLOCKS.register(modEventBus);
        IMItems.register(modEventBus);
        IMRecipe.register(modEventBus);
        IMSounds.REGISTER.register(modEventBus);
        IMMultiblockLogic.init(modEventBus);
        modEventBus.addListener(this::commonSetup);

        if (dist.isClient()) {
            IMClient.init();
        }

        IMStructureSource.COILS.put(IEBlocks.MetalDecoration.MV_COIL, id("mv_coil"));
        IMStructureSource.COILS.put(IEBlocks.MetalDecoration.HV_COIL, id("hv_coil"));
        IMMultiblocks.init();
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        IMWireTypes.setup();
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static ResourceLocation guiId(String path) {
        return ImmersiveMechanical.id("textures/gui/%s.png".formatted(path));
    }
}
