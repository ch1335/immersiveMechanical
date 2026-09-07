package com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel;

import blusunrize.immersiveengineering.api.multiblocks.MultiblockAdvancementTrigger;
import blusunrize.immersiveengineering.api.multiblocks.MultiblockHandler;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockBE;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.common.items.HammerItem;
import blusunrize.immersiveengineering.common.register.IEDataComponents;
import blusunrize.immersiveengineering.common.register.IEItems;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.endpoint.EndPointLogic;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.endpoint.EndPointTemplate;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.templateMultiblocks.TestAbleTemplateMultiblock;
import com.chen1335.immersiveMechanical.config.IMServerConfig;
import com.chen1335.immersiveMechanical.definitions.IMMultiblocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = ImmersiveMechanical.MODID)
public class FormHandler {
    public static final List<TestAbleTemplateMultiblock> ENDPOINTS = new ArrayList<>();
    public static final List<TestAbleTemplateMultiblock> MODULES = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void RightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack itemStack = event.getItemStack();
        if (event.isCanceled() || !itemStack.is(IEItems.Tools.HAMMER.asItem()) || event.getUseItem().isFalse()) {
            return;
        }

        Player player = event.getEntity();
        if (player.isSpectator()) {
            return;
        }

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Direction side = event.getFace();
        if (side == null) {
            return;
        }
        Direction multiblockSide = side.getAxis() == Direction.Axis.Y
                ? Direction.fromYRot(player.getYRot()).getOpposite()
                : side;



        int maxLength = 100;
        List<Runnable> runnables = new ArrayList<>();
        List<BlockPos> linkedParts = new ArrayList<>();

        for (TestAbleTemplateMultiblock endpoint : ENDPOINTS) {
            if (endpoint.canForm(level, pos, multiblockSide, player)) {
                runnables.add(() -> {
                    endpoint.createStructure(level, pos, multiblockSide, player);
                    linkedParts.add(endpoint.getMasterPose(level, pos));
                });
            } else if (endpoint == ENDPOINTS.getLast()) {
                return;
            }
        }


        int length = 0;

        for (int i = 0; i <= maxLength; i++) {
            BlockPos otherEndPoint = pos.relative(multiblockSide.getOpposite(), i + 5);

            for (TestAbleTemplateMultiblock module : ENDPOINTS) {
                if (module.canForm(level, otherEndPoint, multiblockSide.getOpposite(), player)) {
                    length = i;
                    runnables.add(() -> {
                        module.createStructure(level, otherEndPoint, multiblockSide.getOpposite(), player);
                        linkedParts.add(module.getMasterPose(level, otherEndPoint));
                    });
                    break;
                }
            }
            if (length > 0) {
                break;
            }
            if (i == maxLength) {
                return;
            }
        }

        for (int i = 0; i < length; i++) {
            BlockPos relative = pos.relative(multiblockSide.getOpposite(), i + 3);
            for (TestAbleTemplateMultiblock modules : MODULES) {
                if (modules.canForm(level, relative, multiblockSide, player)) {
                    runnables.add(() -> {
                        modules.createStructure(level, relative, multiblockSide, player);
                        linkedParts.add(modules.getMasterPose(level, relative));
                    });
                } else if (modules == MODULES.getLast()) {
                    return;
                }
            }
        }

        if (length <= 0) {
            return;
        }

        runnables.forEach(Runnable::run);

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof IMultiblockBE<?> be) {
            EndPointLogic.State state = (EndPointLogic.State) be.getHelper().getContext().getState();
            state.isMaster = true;
            state.linkedParts = linkedParts;
            BlockPos masterPos = be.getHelper().getContext().getLevel().getAbsoluteOrigin();

            for (int i = 0; i <= length; i++) {
                BlockPos partPos = pos.relative(multiblockSide.getOpposite(), i + 3);
                BlockEntity partEntity = level.getBlockEntity(partPos);
                if (partEntity instanceof IMultiblockBE<?> partBe) {
                    IMultiblockState state1 = partBe.getHelper().getContext().getState();
                    if (state1 instanceof FlyWheelPart flyWheelPart) {
                        flyWheelPart.setMasterPos(masterPos);
                        flyWheelPart.linkedParts = linkedParts;
                    }
                }
            }
        }

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    static {
        ENDPOINTS.add((TestAbleTemplateMultiblock) IMMultiblocks.FLYWHEEL_ENDPOINT.multiblock());
        MODULES.add((TestAbleTemplateMultiblock) IMMultiblocks.FLYWHEEL.multiblock());
        MODULES.add((TestAbleTemplateMultiblock) IMMultiblocks.BEARING.multiblock());
    }
}
