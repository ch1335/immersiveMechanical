package com.chen1335.immersiveMechanical.mixins.immersive_mechanical;

import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockItem;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.IMMultiblockItem;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.PartBlocks.IMCoilBlock;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.RegistryAwareItemModelShaper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RegistryAwareItemModelShaper.class)
public class RegistryAwareItemModelShaperMixin {
    @Inject(method = "getItemModel(Lnet/minecraft/world/item/Item;)Lnet/minecraft/client/resources/model/BakedModel;", at = @At("HEAD"))
    private void getItemModel(Item item, CallbackInfoReturnable<BakedModel> cir, @Local(argsOnly = true) LocalRef<Item> itemLocalRef) {
        if (item.getClass() == IMMultiblockItem.class) {
            Block block = ((MultiblockItem) item).getBlock();
            if (block.getClass() == IMCoilBlock.class) {
                Item item1 = ((IMCoilBlock) block).getMaterialBlock().asItem();
                itemLocalRef.set(item1);
            }
        }
    }
}
