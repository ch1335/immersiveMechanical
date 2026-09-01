package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.endpoint;

import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IClientTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.component.IServerTickableComponent;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockBE;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.CapabilityPosition;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.RelativeBlockFace;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.ShapeType;
import blusunrize.immersiveengineering.common.register.IEItems;
import com.chen1335.immersiveMechanical.API.energy.ReSizeAbleEnergyStorage;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlyWheelPart;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlyWheelPartLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.FlywheelMaterial;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.flywheel.FlyWheelLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;

import java.util.Set;
import java.util.function.Function;

public class EndPointLogic extends FlyWheelPartLogic<EndPointLogic.State> implements IServerTickableComponent<EndPointLogic.State>, IClientTickableComponent<EndPointLogic.State> {
    private static final Set<CapabilityPosition> ENERGY_INTERFACE = Set.of(
            new CapabilityPosition(0, 0, 1, RelativeBlockFace.RIGHT),
            new CapabilityPosition(0, 1, 1, RelativeBlockFace.RIGHT),
            new CapabilityPosition(2, 0, 1, RelativeBlockFace.LEFT),
            new CapabilityPosition(2, 1, 1, RelativeBlockFace.LEFT)
    );


    @Override
    public EndPointLogic.State createInitialState(IInitialMultiblockContext<EndPointLogic.State> context) {
        return new State(context);
    }

    @Override
    public ItemInteractionResult click(IMultiblockContext<State> ctx, BlockPos posInMultiblock, Player player, InteractionHand hand, BlockHitResult absoluteHit, boolean isClient) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (itemInHand.is(IEItems.Tools.HAMMER.asItem())) {
            if (!isClient) {
                State state = ctx.getState();
                state.connectionType = state.connectionType == ConnectionType.INPUT ? ConnectionType.OUTPUT : ConnectionType.INPUT;
                ctx.markDirtyAndSync();

                BlockPos absolute = ctx.getLevel().toAbsolute(posInMultiblock);
                BlockState blockState = ctx.getLevel().getBlockState(posInMultiblock);
                ctx.getLevel().getRawLevel().sendBlockUpdated(absolute,blockState,blockState,3);
                ctx.getLevel().getRawLevel().updateNeighbourForOutputSignal(absolute,blockState.getBlock());
            }
            return ItemInteractionResult.SUCCESS;
        }
        return super.click(ctx, posInMultiblock, player, hand, absoluteHit, isClient);
    }

    @Override
    public Function<BlockPos, VoxelShape> shapeGetter(ShapeType forType) {
        return blockPos -> Shapes.block();
    }

    @Override
    public void tickClient(IMultiblockContext<EndPointLogic.State> context) {
        State state = context.getState();
        if (!state.init) {
            state.updateMasterState();
            state.init = true;
        }
        if (context.getLevel().shouldTickModulo(10) && state.masterState == null) {
            state.updateMasterState();
        }

        state.angleOld = state.angle;
        state.angle += state.angularVelocity;
//        if (state.angle >= 360F) {
//            state.angle -= 360F;
//        }
    }

    @Override
    public void registerCapabilities(CapabilityRegistrar<State> register) {
        register.register(Capabilities.EnergyStorage.BLOCK, (state, position) -> {
            if (position.side() == null || ENERGY_INTERFACE.contains(position)) {
                if (state.masterState == null) {
                    state.updateMasterState();
                }
                if (state.masterState != null) {
                    return state.masterState.innerEnergy;
                }
            }
            return null;
        });
    }

    @Override
    public void tickServer(IMultiblockContext<EndPointLogic.State> context) {
        if (context.getState().isMaster) {
            State state = context.getState();
            if (!state.init) {
                state.updateMasterState();
                state.init = true;
                state.innerEnergy.setMaxEnergyStored(0);
                state.linkedParts.forEach(blockPos -> {
                    BlockEntity blockEntity = context.getLevel().getRawLevel().getBlockEntity(blockPos);
                    if (blockEntity instanceof IMultiblockBE<?> be && be.getHelper().getState() instanceof FlyWheelLogic.State state1) {
                        FlywheelMaterial.get(context.getLevel().getRawLevel(), state1.material).ifPresent(holder -> {
                            state.innerEnergy.setMaxEnergyStored(state.innerEnergy.getMaxEnergyStored() + holder.value().maxEnergyStored());
                        });
                    }
                });
            }

            if (context.getLevel().shouldTickModulo(20)) {
                context.markDirtyAndSync();
            }
            state.angularVelocity = (float) state.innerEnergy.getEnergyStored() / state.innerEnergy.getMaxEnergyStored() * 100;
        }
    }


    public static class State extends FlyWheelPart {
        public boolean isMaster = false;
        private float angle;
        private float angleOld;
        private float angularVelocity;
        private boolean init = false;

        public ConnectionType connectionType = ConnectionType.INPUT;
        public ReSizeAbleEnergyStorage innerEnergy = new ReSizeAbleEnergyStorage(0, 8192, 8192);

        public State(IInitialMultiblockContext<? extends FlyWheelPart> context) {
            super(context);
        }


        @Override
        public void writeSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            super.writeSaveNBT(nbt, provider);
            nbt.putFloat("angle", angle);
            nbt.putFloat("angularVelocity", angularVelocity);
            nbt.putBoolean("isMaster", isMaster);
            nbt.putInt("EnergyStored", innerEnergy.getEnergyStored());
            nbt.putString("connectionType", connectionType.name());
        }

        @Override
        public void readSaveNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            super.readSaveNBT(nbt, provider);
            angle = nbt.getFloat("angle");
            angularVelocity = nbt.getFloat("angularVelocity");
            isMaster = nbt.getBoolean("isMaster");
            innerEnergy.setStoredEnergy(nbt.getInt("EnergyStored"));
            connectionType = ConnectionType.valueOf(nbt.getString("connectionType"));
        }


        @Override
        public void writeSyncNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            super.writeSyncNBT(nbt, provider);
            nbt.putFloat("angularVelocity", angularVelocity);
            nbt.putBoolean("isMaster", isMaster);
            nbt.putString("connectionType", connectionType.name());
        }

        @Override
        public void readSyncNBT(CompoundTag nbt, HolderLookup.Provider provider) {
            super.readSyncNBT(nbt, provider);
            angularVelocity = nbt.getFloat("angularVelocity");
            isMaster = nbt.getBoolean("isMaster");
            connectionType = ConnectionType.valueOf(nbt.getString("connectionType"));
        }

        public float getAngle() {
            return masterState != null ? masterState.angle : angle;
        }

        public float getAngleOld() {
            return masterState != null ? masterState.angleOld : angleOld;
        }

        @Override
        public void updateMasterState() {
            if (isMaster) {
                masterState = this;
                return;
            }
            super.updateMasterState();
        }
    }

    public enum ConnectionType {
        INPUT,
        OUTPUT;
    }
}
