package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.flywheel;

import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IClientTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockBlockEntityMaster;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.ShapeType;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.interfaces.MBMemorizeStructure;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlyWheelPart;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlyWheelPartLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.shapes.FlyWheelShape;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class FlyWheelLogic extends FlyWheelPartLogic<FlyWheelLogic.State> implements IClientTickableComponent<FlyWheelLogic.State>, MBMemorizeStructure<FlyWheelLogic.State> {
    @Override
    public void tickClient(IMultiblockContext<FlyWheelLogic.State> context) {
        FlyWheelLogic.State state = context.getState();
        if (context.getLevel().shouldTickModulo(10) && state.masterState == null) {
            state.updateMasterState();
        }
    }

    @Override
    public FlyWheelLogic.State createInitialState(IInitialMultiblockContext<FlyWheelLogic.State> context) {
        return new State(context);
    }

    @Override
    public Function<BlockPos, VoxelShape> shapeGetter(ShapeType forType) {
        return new FlyWheelShape();
    }

    @Override
    public void setMemorizedBlockState(State state, BlockPos pos, BlockState blockState) {

    }

    @Override
    public BlockState getMemorizedBlockState(State state, BlockPos pos) {
        if (pos.getX() == 1 && pos.getY() == 1) {
            return null;
        }
        return state.material.defaultBlockState();
    }

    public static class State extends FlyWheelPart {
        public Function<MultiblockBlockEntityMaster<State>, AABB> renderBoundingBox = Util.memoize(be -> AABB.ofSize(Vec3.atLowerCornerOf(be.getBlockPos()), 2, 2, 2));

        public Block material = Blocks.IRON_BLOCK;


        public State(IInitialMultiblockContext<State> context) {
            super(context);
        }

        public float getAngle() {
            return masterState != null ? masterState.getAngle() : 0;
        }

        public float getAngleOld() {
            return masterState != null ? masterState.getAngleOld() : 0;
        }

        @Override
        public void writeSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            super.writeSaveNBT(nbt, provider);
            BuiltInRegistries.BLOCK.getResourceKey(material).ifPresent(blockResourceKey -> {
                nbt.putString("material", blockResourceKey.location().toString());
            });
        }

        @Override
        public void readSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            super.readSaveNBT(nbt, provider);
            if (nbt.contains("material")) {
                BuiltInRegistries.BLOCK.getHolder(ResourceLocation.parse(nbt.getString("material"))).ifPresent(blockReference -> {
                    material = blockReference.value();
                });
            }
        }

        @Override
        public void writeSyncNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            super.writeSyncNBT(nbt, provider);
            BuiltInRegistries.BLOCK.getResourceKey(material).ifPresent(blockResourceKey -> {
                nbt.putString("material", blockResourceKey.location().toString());
            });
        }

        @Override
        public void readSyncNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            super.readSyncNBT(nbt, provider);
            if (nbt.contains("material")) {
                BuiltInRegistries.BLOCK.getHolder(ResourceLocation.parse(nbt.getString("material"))).ifPresent(blockReference -> {
                    material = blockReference.value();
                });
            }
        }

        public Block getMaterial() {
            return material;
        }
    }
}
