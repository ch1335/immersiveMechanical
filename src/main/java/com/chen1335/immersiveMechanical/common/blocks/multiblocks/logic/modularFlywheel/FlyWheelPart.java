package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel;

import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockBE;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.endpoint.EndPointLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FlyWheelPart implements IMultiblockState {
    protected final Supplier<Level> levelSupplier;
    protected BlockPos masterPos = null;

    public boolean beingDisassembled = false;

    public EndPointLogic.State masterState = null;

    public List<BlockPos> linkedParts = new ArrayList<>();

    public FlyWheelPart(IInitialMultiblockContext<? extends FlyWheelPart> context) {
        levelSupplier = context.levelSupplier();
    }

    @Override
    public void writeSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
        writeMasterNBT(nbt, provider);
        BlockPos.CODEC.listOf().encodeStart(NbtOps.INSTANCE, linkedParts).ifSuccess(tag -> {
            nbt.put("linkedParts", tag);
        });
    }

    @Override
    public void readSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
        readMasterNBT(nbt, provider);
        BlockPos.CODEC.listOf().decode(NbtOps.INSTANCE, nbt.get("linkedParts")).ifSuccess(data -> {
            linkedParts = data.getFirst();
        });
    }

    @Override
    public void writeSyncNBT(CompoundTag nbt, HolderLookup.Provider provider) {
        writeMasterNBT(nbt, provider);
    }

    @Override
    public void readSyncNBT(CompoundTag nbt, HolderLookup.Provider provider) {
        readMasterNBT(nbt, provider);
    }

    public void writeMasterNBT(CompoundTag nbt, HolderLookup.Provider provider) {
        BlockPos masterPos = getMasterPos();
        if (masterPos != null) {
            CompoundTag masterPosTag = new CompoundTag();
            masterPosTag.putInt("x", masterPos.getX());
            masterPosTag.putInt("y", masterPos.getY());
            masterPosTag.putInt("z", masterPos.getZ());
            nbt.put("masterPos", masterPosTag);
        }
    }

    public void readMasterNBT(CompoundTag nbt, HolderLookup.Provider provider) {
        if (nbt.contains("masterPos")) {
            CompoundTag masterPosTag = nbt.getCompound("masterPos");
            BlockPos masterPos = new BlockPos(
                    masterPosTag.getInt("x"),
                    masterPosTag.getInt("y"),
                    masterPosTag.getInt("z"));
            setMasterPos(masterPos);
        }
    }

    public void setMasterPos(BlockPos masterPos) {
        this.masterPos = masterPos;
    }

    public BlockPos getMasterPos() {
        return this.masterPos;
    }

    public void updateMasterState() {
        if (masterState == null && masterPos != null) {
            Level level = levelSupplier.get();
            if (level != null) {
                BlockEntity blockEntity = level.getBlockEntity(masterPos);
                if (blockEntity instanceof IMultiblockBE<?> be) {
                    IMultiblockState iMultiblockState = be.getHelper().getContext().getState();
                    if (iMultiblockState instanceof EndPointLogic.State state) {
                        masterState = state;
                    }
                }
            }
        }
    }
}
