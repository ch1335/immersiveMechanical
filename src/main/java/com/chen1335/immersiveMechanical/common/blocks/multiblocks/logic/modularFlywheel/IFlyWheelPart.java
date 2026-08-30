package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public interface IFlyWheelPart {
    void setMasterPos(BlockPos masterPose);

    BlockPos getMasterPos();


    default void writeMasterNBT(CompoundTag nbt, HolderLookup.Provider provider) {
        BlockPos masterPos = getMasterPos();
        if (masterPos != null) {
            CompoundTag masterPosTag = new CompoundTag();
            masterPosTag.putInt("x", masterPos.getX());
            masterPosTag.putInt("y", masterPos.getY());
            masterPosTag.putInt("z", masterPos.getZ());
            nbt.put("masterPos", masterPosTag);
        }
    }

    default void readMasterNBT(CompoundTag nbt, HolderLookup.Provider provider) {
        if (nbt.contains("masterPos")) {
            CompoundTag masterPosTag = nbt.getCompound("masterPos");
            BlockPos masterPos = new BlockPos(
                    masterPosTag.getInt("x"),
                    masterPosTag.getInt("y"),
                    masterPosTag.getInt("z"));
            setMasterPos(masterPos);
        }
    }
}
