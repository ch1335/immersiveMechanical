package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel;

import blusunrize.immersiveengineering.common.items.HammerItem;
import blusunrize.immersiveengineering.common.register.IEDataComponents;
import blusunrize.immersiveengineering.common.register.IEItems;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = ImmersiveMechanical.MODID)
public class FormHandler {
    @SubscribeEvent
    public static void RightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack itemStack = event.getItemStack();
        if (itemStack.is(IEItems.Tools.HAMMER.asItem())) {
            HammerItem.MultiblockRestriction restriction = itemStack.getOrDefault(IEDataComponents.MULTIBLOCK_RESTRICTION, HammerItem.MultiblockRestriction.DEFAULT);

        }
    }
}
