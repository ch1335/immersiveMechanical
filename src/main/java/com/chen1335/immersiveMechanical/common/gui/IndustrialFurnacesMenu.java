package com.chen1335.immersiveMechanical.common.gui;

import blusunrize.immersiveengineering.api.energy.MutableEnergyStorage;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessInMachine;
import blusunrize.immersiveengineering.common.gui.IEContainerMenu;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.gui.sync.GenericContainerData;
import blusunrize.immersiveengineering.common.gui.sync.GetterAndSetter;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.industrialFurnace.IndustrialFurnacesLogic;
import com.chen1335.immersiveMechanical.common.gui.sync.IMGenericDataSerializers;
import com.chen1335.immersiveMechanical.recipe.IndustrialFurnaceRecipe;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.List;

public class IndustrialFurnacesMenu extends IEContainerMenu {
    public final MutableEnergyStorage energyStorage;
    public final GetterAndSetter<List<SlotProgress>> processes;

    protected IndustrialFurnacesMenu(MenuContext ctx, Inventory inventory, ItemStackHandler inv, MutableEnergyStorage energyStorage, GetterAndSetter<List<SlotProgress>> processes) {
        super(ctx);
        this.energyStorage = energyStorage;
        this.processes = processes;
        Level level = inventory.player.level();
        for (int i = 0; i < 9; i++)
            this.addSlot(new InputSlot(inv, i, 10 + i % 3 * 21, 18 + i / 3 * 18, level));

        for (int i = 0; i < 9; i++)
            this.addSlot(new IESlot.NewOutput(inv, 9 + i, 101 + i % 3 * 18, 18 + i / 3 * 18));

        ownSlotCount = 18;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++)
                addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 85 + i * 18));
        for (int i = 0; i < 9; i++)
            addSlot(new Slot(inventory, i, 8 + i * 18, 143));

        addGenericData(GenericContainerData.energy(energyStorage));
        addGenericData(new GenericContainerData<>(IMGenericDataSerializers.INDUSTRIAL_FURNACES_PROCESS_SLOTS, processes));
    }

    public static IndustrialFurnacesMenu makeServer(MenuType<IndustrialFurnacesMenu> menuMenuType, int i, Inventory inventory, MultiblockMenuContext<IndustrialFurnacesLogic.State> context) {
        IndustrialFurnacesLogic.State state = context.mbContext().getState();

        GetterAndSetter<List<SlotProgress>> getterOnly = GetterAndSetter.getterOnly(() -> state.getProcessQueue().stream()
                .filter(multiblockProcess -> multiblockProcess instanceof MultiblockProcessInMachine<IndustrialFurnaceRecipe>)
                .map(multiblockProcess -> SlotProgress.fromCtx((MultiblockProcessInMachine<IndustrialFurnaceRecipe>) multiblockProcess, context))
                .toList());


        return new IndustrialFurnacesMenu(
                multiblockCtx(menuMenuType, i, context),
                inventory,
                state.getInventory(),
                context.mbContext().getState().getEnergy(),
                getterOnly
        );
    }

    public static IndustrialFurnacesMenu makeClient(MenuType<IndustrialFurnacesMenu> menuMenuType, int i, Inventory inventory) {
        return new IndustrialFurnacesMenu(
                clientCtx(menuMenuType, i),
                inventory,
                new ItemStackHandler(18),
                new MutableEnergyStorage(IndustrialFurnacesLogic.ENERGY_CAPACITY),
                GetterAndSetter.standalone(List.of())
        );
    }


    public record SlotProgress(int slot, float progress) {
        public static final StreamCodec<ByteBuf, SlotProgress> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, SlotProgress::slot,
                ByteBufCodecs.FLOAT, SlotProgress::progress,
                SlotProgress::new
        );

        public static SlotProgress fromCtx(MultiblockProcessInMachine<IndustrialFurnaceRecipe> process, MultiblockMenuContext<IndustrialFurnacesLogic.State> context) {
            int inputSlot = process.getInputSlots()[0];
            float progress = ((float) process.processTick) / ((float) process.getMaxTicks(context.mbContext().getLevel().getRawLevel()));
            return new SlotProgress(inputSlot, progress);
        }

        public static SlotProgress read(FriendlyByteBuf buf) {
            return STREAM_CODEC.decode(buf);
        }

        public static void write(FriendlyByteBuf buf, SlotProgress progress) {
            STREAM_CODEC.encode(buf, progress);
        }
    }

    public static class InputSlot extends SlotItemHandler {
        private final Level level;

        public InputSlot(ItemStackHandler inv, int slot, int x, int y, Level level) {
            super(inv, slot, x, y);
            this.level = level;
        }

        @Override
        public boolean mayPlace(ItemStack itemStack) {
            return !itemStack.isEmpty() && IndustrialFurnaceRecipe.isValidRecipeInput(level, itemStack);
        }
    }
}
