package com.chen1335.immersiveMechanical.common.blockEntities;

import blusunrize.immersiveengineering.common.blocks.BlockCapabilityRegistration;
import blusunrize.immersiveengineering.common.blocks.metal.TurretBlockEntity;
import blusunrize.immersiveengineering.common.network.MessageBlockEntitySync;
import blusunrize.immersiveengineering.common.register.IEMenuTypes;
import blusunrize.immersiveengineering.common.util.FakePlayerUtil;
import blusunrize.immersiveengineering.common.util.sound.MultiblockSound;
import com.chen1335.immersiveMechanical.API.objects.IMDamageTypes;
import com.chen1335.immersiveMechanical.API.objects.IMMenuTypes;
import com.chen1335.immersiveMechanical.API.objects.IMSounds;
import com.chen1335.immersiveMechanical.mixinsAPI.IEnergyStorageMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BooleanSupplier;

public class TurretLaserBlockEntity extends TurretBlockEntity<TurretLaserBlockEntity> {
    public static int ENERGY_CAPACITY = 1000000;

    public float rotationYawOld = 0;

    public float rotationPitchOld = 0;

    public boolean isActive = false;

    private BooleanSupplier isPlayingSound = () -> false;

    public TurretLaserBlockEntity(BlockEntityType<TurretLaserBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        ((IEnergyStorageMixin) energyStorage).im$setNewCap(ENERGY_CAPACITY);
    }

    @Override
    protected double getRange() {
        return 33;
    }

    @Override
    protected boolean canActivate() {
        return energyStorage.getEnergyStored() >= 1024;
    }

    @Override
    protected int getChargeupTicks() {
        return 4;
    }

    @Override
    protected int getActiveTicks() {
        return 1;
    }

    @Override
    protected boolean loopActivation() {
        return true;
    }


    @Override
    protected void activate() {
        if (!isActive) {
            isActive = true;
            sendRenderPacket();
        }
        if (tick > 6) {
            final Vec3 turret = new Vec3(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1.5, this.worldPosition.getZ() + 0.5);

            Vec3 targetVector = getTargetVector(target);
            double d0 = Double.MAX_VALUE;
            Entity entity = null;
            for (Entity entity1 : level.getEntities((Entity) null, new AABB(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), target.getX(), target.getY(), target.getZ()).inflate(2), EntitySelector.LIVING_ENTITY_STILL_ALIVE)) {
                AABB aabb = entity1.getBoundingBox();
                Optional<Vec3> optional = aabb.clip(turret, targetVector);
                if (optional.isPresent()) {
                    double d1 = turret.distanceToSqr(optional.get());
                    if (d1 < d0) {
                        d0 = d1;
                        entity = entity1;
                    }
                }
            }

            if (entity != null) {
                LivingEntity livingEntity = (LivingEntity) entity;
                LivingEntity lastHurtByMob = livingEntity.getLastHurtByMob();

                entity.hurt(this.level.damageSources().source(IMDamageTypes.LASER, FakePlayerUtil.getFakePlayer(level)), 0.25F);
                entity.setRemainingFireTicks(100);
                livingEntity.setLastHurtByMob(lastHurtByMob);

            }
        }
    }


    protected void sendRenderPacket() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("active", isActive);
        PacketDistributor.sendToPlayersTrackingChunk(
                (ServerLevel) level, new ChunkPos(worldPosition), new MessageBlockEntitySync(getBlockPos(), tag)
        );

    }

    @Override
    public void receiveMessageFromServer(CompoundTag tag) {
        isActive = tag.getBoolean("active");
    }

    @Override
    public boolean isValidTarget(@NotNull LivingEntity entity, boolean checkCanShoot) {
        double range = getRange();
        return super.isValidTarget(entity, checkCanShoot) && !entity.isDeadOrDying() && getGunToTargetVec(entity).lengthSqr() < (range - 1) * (range - 1);
    }

    public float beamLength = 32;

    @Override

    public void tickClient() {
        rotationPitchOld = rotationPitch;
        rotationYawOld = rotationYaw;
        super.tickClient();
        beamLength = 32;
        final Vec3 turret = new Vec3(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1.5, this.worldPosition.getZ() + 0.5);


        if (!isPlayingSound.getAsBoolean()) {
            isPlayingSound = MultiblockSound.startSound(
                    () -> this.isActive, () -> !this.isRemoved(), turret, IMSounds.LASER_TURRET_BEAM, 0.375f
            );
        }


        if (target != null) {
            Vec3 targetVector = getTargetVector(target);
            HitResult hitresult = this.level.clip(new ClipContext(turret, targetVector, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty()));
            Vec3 location = hitresult.getLocation();

            if (hitresult.getType() != HitResult.Type.MISS) {
                beamLength = (float) turret.distanceTo(location) - 0.7F;
            }

            double d0 = Double.MAX_VALUE;
            Optional<Vec3> entityClip = Optional.empty();
            for (Entity entity1 : level.getEntities((Entity) null, new AABB(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), target.getX(), target.getY(), target.getZ()).inflate(2), EntitySelector.LIVING_ENTITY_STILL_ALIVE)) {
                AABB aabb = entity1.getBoundingBox();
                Optional<Vec3> optional = aabb.clip(turret, targetVector);
                if (optional.isPresent()) {
                    double d1 = turret.distanceToSqr(optional.get());
                    if (d1 < d0) {
                        d0 = d1;
                        entityClip = optional;
                    }
                }
            }

            entityClip.ifPresent(vec3 -> beamLength = (float) turret.distanceTo(vec3));
        }

    }

    @Override
    public void tickServer() {
        super.tickServer();
        if (isActive && target == null) {
            isActive = false;
            sendRenderPacket();
        }
        if (isActive) {
            energyStorage.extractEnergy(1024, false);
            if (energyStorage.getEnergyStored() <= 1024) {
                isActive = false;
                sendRenderPacket();
            }
        }
    }

    @Override
    public IEMenuTypes.@NotNull ArgContainer<? super TurretLaserBlockEntity, ?> getContainerType() {
        return IMMenuTypes.LASER_TURRET;
    }

    public static void registerCapabilities(BlockCapabilityRegistration.BECapabilityRegistrar<TurretLaserBlockEntity> registrar) {
        TurretBlockEntity.registerCapabilitiesBase(registrar);
    }
}
