package com.chen1335.immersiveMechanical.attachmentDatas;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public class IMBEAttachmentData implements INBTSerializable<CompoundTag> {
    public BlockState originalBlock;

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        if (originalBlock != null) {
            tag.put("originalBlock", NbtUtils.writeBlockState(originalBlock));
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        if (nbt.contains("originalBlock")) {
            originalBlock = NbtUtils.readBlockState(provider.lookupOrThrow(Registries.BLOCK), nbt.getCompound("originalBlock"));
        }
    }
}
