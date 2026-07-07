package com.chen1335.immersiveMechanical.conveyorBeltInnovation;

import blusunrize.immersiveengineering.api.tool.conveyor.BasicConveyorType;
import blusunrize.immersiveengineering.api.tool.conveyor.IConveyorType;
import blusunrize.immersiveengineering.client.render.conveyor.BasicConveyorRender;
import blusunrize.immersiveengineering.common.blocks.metal.conveyors.ConveyorBase;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class AdvanceBasicConveyor extends ConveyorBase {
    public static final ResourceLocation NAME = ImmersiveMechanical.id("advance_basic");

    public AdvanceBasicConveyor(BlockEntity tile) {
        super(tile);
    }

    public static final IConveyorType<AdvanceBasicConveyor> TYPE = new BasicConveyorType<>(
            NAME, false, true, AdvanceBasicConveyor::new, () -> new BasicConveyorRender<>(texture_on, texture_off)
    );

    @Override
    public void onEntityCollision(@NotNull Entity entity) {
        super.onEntityCollision(entity);
    }

    @Override
    public IConveyorType<AdvanceBasicConveyor> getType() {
        return TYPE;
    }
}
