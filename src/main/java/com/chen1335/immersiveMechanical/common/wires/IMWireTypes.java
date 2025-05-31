package com.chen1335.immersiveMechanical.common.wires;

import blusunrize.immersiveengineering.api.wires.Connection;
import blusunrize.immersiveengineering.api.wires.WireApi;
import blusunrize.immersiveengineering.api.wires.WireType;
import blusunrize.immersiveengineering.api.wires.localhandlers.EnergyTransferHandler;
import com.chen1335.immersiveMechanical.API.objects.IMBlocks;
import com.chen1335.immersiveMechanical.API.objects.IMItems;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class IMWireTypes {
    public static WireType EHV = new IMEnergyWire(Tier.EHV);
    public static WireType UHV = new IMEnergyWire(Tier.UHV);

    public static void setup() {
        WireApi.registerFeedthroughForWiretype(EHV, ImmersiveMechanical.id("block/connector/connector_ehv"), new double[]{0.0, 4.0, 8.0, 12.0}, 0.75, IMBlocks.CONNECTOR_EHV.get().defaultBlockState());
    }

    private enum Tier {
        EHV("EHV", 131072, 0.0002, 64, 1.003, 0Xcae1ed, IMItems.EHV_WIRE_COIL),
        UHV("UHV", 2097152, 0.00004, 1024, 1.001, 0X9badb7, IMItems.UHV_WIRE_COIL);
        public final String name;
        public final int transferRate;
        public final double basicLossRate;
        public final int maxLength;
        public final double slack;
        public final int color;
        public final Holder<Item> wireCoilItem;

        Tier(String name, int transferRate, double basicLossRate, int maxLength, double slack, int color, Holder<Item> wireCoilItem) {
            this.name = name;
            this.transferRate = transferRate;
            this.basicLossRate = basicLossRate;
            this.maxLength = maxLength;
            this.slack = slack;
            this.color = color;
            this.wireCoilItem = wireCoilItem;
        }
    }


    private static class IMEnergyWire extends WireType implements EnergyTransferHandler.IEnergyWire {
        private final Tier tier;

        private IMEnergyWire(Tier tier) {
            this.tier = tier;
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
