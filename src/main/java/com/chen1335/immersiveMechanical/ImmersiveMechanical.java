package com.chen1335.immersiveMechanical;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.data.BiomeTags;
import com.chen1335.immersiveMechanical.API.objects.IMAttachmentTypes;
import com.chen1335.immersiveMechanical.API.objects.IMSounds;
import com.chen1335.immersiveMechanical.client.IMClient;
import com.chen1335.immersiveMechanical.common.wires.IMWireTypes;
import com.chen1335.immersiveMechanical.config.IMServerConfig;
import com.chen1335.immersiveMechanical.definitions.*;
import com.chen1335.immersiveMechanical.recipe.transters.IRecipeTransfer;
import com.chen1335.immersiveMechanical.recipe.transters.IndustrialFurnaceRecipeTransfer;
import com.chen1335.immersiveMechanical.recipe.transters.PyrolyseOvenRecipeTransfer;
import com.chen1335.registrate.IERegistrate;
import com.mojang.logging.LogUtils;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.List;

@Mod(ImmersiveMechanical.MODID)
public class ImmersiveMechanical {
    public static final String MODID = "immersive_mechanical";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final IERegistrate REGISTRATE = IERegistrate.create(MODID);

    private static final List<IRecipeTransfer> RECIPE_TRANSFERS = List.of(
            IndustrialFurnaceRecipeTransfer.INSTANCE,
            PyrolyseOvenRecipeTransfer.INSTANCE
    );

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static RegistryEntry<CreativeModeTab, CreativeModeTab> TAB = REGISTRATE.defaultCreativeTab("immersive_mechanical",
                    builder -> builder
                            .icon(() -> IMMultiblocks.GREEN_HOUSE.getBlockItem().getDefaultInstance())
                            .title(Component.translatable("itemGroup.immersive_mechanical"))
                            .build())
            .register();

    public ImmersiveMechanical(IEventBus modEventBus, Dist dist, ModContainer modContainer) {
        REGISTRATE.registerEventListeners(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        IMItems.register(modEventBus);
        IMRecipe.init();
        IMSounds.REGISTER.register(modEventBus);
        IMAttachmentTypes.ATTACHMENT_TYPES.register(modEventBus);
        modEventBus.addListener(this::commonSetup);

        if (dist.isClient()) {
            IMClient.init();
        }
        IMMetals.init();
        IMItems.init();
        IMBlocks.init();
        IMMultiblocks.init();

        modContainer.registerConfig(ModConfig.Type.SERVER, IMServerConfig.CONFIG_SPEC);
        IMServerConfig.MACHINES.setUpConfig();
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

    public static List<IRecipeTransfer> getRecipeTransfer() {
        return RECIPE_TRANSFERS;
    }
}
