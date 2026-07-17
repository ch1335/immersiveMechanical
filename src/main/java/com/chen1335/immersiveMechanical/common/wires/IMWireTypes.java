package com.chen1335.immersiveMechanical.common.wires;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.api.tool.IElectricEquipment;
import blusunrize.immersiveengineering.api.wires.Connection;
import blusunrize.immersiveengineering.api.wires.WireType;
import blusunrize.immersiveengineering.api.wires.localhandlers.WireDamageHandler;
import com.chen1335.immersiveMechanical.definitions.IMBlocks;
import com.chen1335.immersiveMechanical.definitions.IMItems;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static blusunrize.immersiveengineering.api.wires.WireApi.registerFeedthroughForWiretype;

public class IMWireTypes {
    public static WireType EHV = new IMEnergyWire(Tier.EHV);
//    public static WireType UHV = new IMEnergyWire(Tier.UHV);

    public static void setup() {

        registerFeedthroughForWiretype(EHV, IEApi.ieLoc("block/connector/connector_hv"),
                new double[]{0, 4, 8, 12}, 0.6875F,
                IMBlocks.CONNECTOR_EHV.get().defaultBlockState());

    }

    private enum Tier {
        EHV("EHV", 131072, 0.0002, 64, 1.003, 0Xcae1ed, IMItems.EHV_WIRE_COIL, 0.3, 2.0F, 30.0F);
        //        UHV("UHV", 2097152, 0.00004, 1024, 1.001, 0X9badb7, IMItems.UHV_WIRE_COIL, 0.3, 2.5F, 50.0F);
        public final String name;
        public final int transferRate;
        public final double basicLossRate;
        public final int maxLength;
        public final double slack;
        public final int color;
        public final Holder<Item> wireCoilItem;
        /**
         * 碰撞检测时给实体包围盒 inflate 的半径,>0 才会产生电击伤害
         */
        public final double damageRadius;
        /**
         * 电源等级,用于 IElectricEquipment(护甲等)的电击效果计算
         */
        public final float sourceLevel;
        /**
         * 基础伤害值,实际伤害 = baseDamage * 当前能量 / transferRate * 8.0
         */
        public final float baseDamage;

        Tier(String name, int transferRate, double basicLossRate, int maxLength, double slack, int color, Holder<Item> wireCoilItem, double damageRadius, float sourceLevel, float baseDamage) {
            this.name = name;
            this.transferRate = transferRate;
            this.basicLossRate = basicLossRate;
            this.maxLength = maxLength;
            this.slack = slack;
            this.color = color;
            this.wireCoilItem = wireCoilItem;
            this.damageRadius = damageRadius;
            this.sourceLevel = sourceLevel;
            this.baseDamage = baseDamage;
        }
    }


    private static class IMEnergyWire extends WireType implements WireDamageHandler.IShockingWire {
        private final Tier tier;
        private final IElectricEquipment.ElectricSource eSource;

        private IMEnergyWire(Tier tier) {
            this.tier = tier;
            this.eSource = new IElectricEquipment.ElectricSource(tier.sourceLevel);
        }

        /**
         * 让电线所在网络注册 WireDamageHandler,否则实体碰到电线不会触发 onCollided
         */
        @Override
        public Collection<ResourceLocation> getRequestedHandlers() {
            return ImmutableList.of(WireDamageHandler.ID);
        }

        @Override
        public double getDamageRadius() {
            return tier.damageRadius;
        }

        @Override
        public IElectricEquipment.ElectricSource getElectricSource() {
            return eSource;
        }

        /**
         * 实际伤害。energy 由 WireDamageHandler 传入,为 min(网络当前可用能量, transferRate)。
         * 即电线只有在传输能量时才会电击,与 IE 原版一致。
         */
        @Override
        public float getDamageAmount(Entity entity, Connection connection, int energy) {
            return tier.baseDamage * energy / getTransferRate() * 8.0F;
        }

        @Override
        public int getTransferRate() {
            return tier.transferRate;
        }

        @Override
        public double getBasicLossRate(Connection connection) {
            return 0.003 * connection.getLength() / (double) this.getMaxLength();
        }

        @Override
        public double getLossRate(Connection connection, int i) {
            return 0;
        }

        @Override
        public String getUniqueName() {
            return tier.name;
        }

        @Override
        public int getColour(Connection connection) {
            return tier.color;
        }

        @Override
        public double getSlack() {
            return tier.slack;
        }

        @Override
        public int getMaxLength() {
            return tier.maxLength;
        }

        @Override
        public ItemStack getWireCoil(Connection connection) {
            return tier.wireCoilItem.value().getDefaultInstance();
        }

        @Override
        public double getRenderDiameter() {
            return 0.0625;
        }

        @Override
        public @NotNull String getCategory() {
            return tier.name;
        }
    }
}
