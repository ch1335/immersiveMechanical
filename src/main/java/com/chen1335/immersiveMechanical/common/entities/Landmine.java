package com.chen1335.immersiveMechanical.common.entities;

import com.chen1335.immersiveMechanical.util.SimpleSchedule;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class
Landmine extends Entity {
    private static final AABB ZERO_AABB = new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    private static final EntityDimensions FUSE_SIZE = EntityDimensions.scalable(0.5625F, 0.0625F);
    private AABB fuseAABB = ZERO_AABB;

    private static final EntityDataAccessor<BlockState> DISGUISE = SynchedEntityData.defineId(Landmine.class, EntityDataSerializers.BLOCK_STATE);

    public Landmine(EntityType<?> entityType, Level level) {
        super(entityType, level);
        blocksBuilding = true;
    }

    private boolean triggered = false;

    @Override
    public void tick() {
        super.tick();
        if (verticalCollisionBelow) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.2, 1, 0.2));
        }
        applyGravity();
        if (!level().isClientSide) {
            pushNearbyEntities();
        }
        this.move(MoverType.SELF, this.getDeltaMovement());

        if (!triggered) {
            List<Entity> entities = level().getEntities(this, getFuseAABB(), entity -> {
                if (entity.isSpectator()) {
                    return false;
                }
                EntityDimensions dimensions = entity.getType().getDimensions();
                return dimensions.width() * dimensions.height() > 1;
            });

            if (!entities.isEmpty()) {
                triggered = true;
                level().playSound(null, getX(), getY(), getZ(), BlockSetType.IRON.pressurePlateClickOff(), SoundSource.HOSTILE);
                if (!level().isClientSide) {
                    SimpleSchedule.addSchedule(level(), new SimpleSchedule.Wait(this::doExplode, 4));
                }
            }
        }

    }

    private void doExplode() {
        if (!isRemoved()) {
            this.level().explode(
                    this,
                    getX(),
                    getY(),
                    getZ(),
                    3,
                    Level.ExplosionInteraction.NONE);
            this.discard();
        }
    }

    public void setDisguise(BlockState blockState) {
        entityData.set(DISGUISE, blockState);
    }

    public BlockState getDisguise() {
        return entityData.get(DISGUISE);
    }

    @Override
    public void setPos(double x, double y, double z) {
        super.setPos(x, y, z);
        this.fuseAABB = FUSE_SIZE.makeBoundingBox(getX(), getY() + getType().getDimensions().height(), getZ());
    }

    private void pushNearbyEntities() {
        for (Entity entity : level().getEntities(this, getBoundingBox().inflate(0.01D, -0.01D, 0.01D), EntitySelector.pushableBy(this))) {
            if (!entity.hasPassenger(this)) {
                this.push(entity);
            }
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return !isRemoved();
    }

    @Override
    public boolean isPushable() {
        return !isRemoved();
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return (entity.canBeCollidedWith() || entity.isPushable()) && !this.isPassengerOfSameVehicle(entity);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.06;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DISGUISE, Blocks.AIR.defaultBlockState());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        BlockState.CODEC.decode(NbtOps.INSTANCE, compound.get("disguise")).ifSuccess(p -> {
            entityData.set(DISGUISE, p.getFirst());
        });
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        BlockState.CODEC.encodeStart(NbtOps.INSTANCE, entityData.get(DISGUISE)).ifSuccess(tag -> {
            compound.put("disguise", tag);
        });
    }

    public AABB getFuseAABB() {
        return fuseAABB;
    }
}
