package com.chen1335.immersiveMechanical.network;

import blusunrize.immersiveengineering.api.multiblocks.blocks.registry.MultiblockBlockEntityMaster;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.GreenHouseLogic;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record GreenHouseGrowsPack(BlockPos blockPos, List<Float> list) implements CustomPacketPayload {
    public static final Type<GreenHouseGrowsPack> TYPE = new Type<>(ImmersiveMechanical.id("green_house_grows"));

    public static final StreamCodec<ByteBuf, GreenHouseGrowsPack> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            GreenHouseGrowsPack::blockPos,
            ByteBufCodecs.FLOAT.apply(ByteBufCodecs.list()),
            GreenHouseGrowsPack::list,
            GreenHouseGrowsPack::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        Level level = context.player().level();
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof MultiblockBlockEntityMaster<?> master) {
            GreenHouseLogic.State state = (GreenHouseLogic.State) master.getHelper().getState();
            for (int i = 0; i < list.size(); i++) {
                state.processUnits.get(i).growth = list.get(i);
            }
        }
    }
}
