package com.chen1335.immersiveMechanical.client.gui;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.client.gui.IEContainerScreen;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import blusunrize.immersiveengineering.client.gui.info.EnergyInfoArea;
import blusunrize.immersiveengineering.client.gui.info.InfoArea;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.client.IMClient;
import com.chen1335.immersiveMechanical.common.gui.SmallMiningMachineMenu;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;


public class SmallMiningMachineScreen extends IEContainerScreen<SmallMiningMachineMenu> {
    private static final ResourceLocation TEXTURE = ImmersiveMechanical.guiId("small_mining_machine");

    private static final GuiButtonIE.ButtonTexture RESET = new GuiButtonIE.ButtonTexture(
            ImmersiveMechanical.id("reset"), ImmersiveMechanical.id("reset")
    );
    private GuiButtonIE reset;

    public SmallMiningMachineScreen(SmallMiningMachineMenu inventorySlotsIn, Inventory inv, Component title) {
        super(inventorySlotsIn, inv, title, TEXTURE);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        BlockPos blockPos = menu.current.get();
        graphics.drawString(font, "X:%s".formatted(blockPos.getX()), leftPos + 7, topPos + 45, Lib.COLOUR_I_ImmersiveOrange);
        graphics.drawString(font, "Y:%s".formatted(blockPos.getY()), leftPos + 7, topPos + 55, Lib.COLOUR_I_ImmersiveOrange);
        graphics.drawString(font, "Z:%s".formatted(blockPos.getZ()), leftPos + 7, topPos + 65, Lib.COLOUR_I_ImmersiveOrange);
        int i = 0;
        if (menu.active.get()) {
            i = IMClient.TICKED % 3;
        }
        graphics.blit(TEXTURE, leftPos + 34, topPos + 55, 176 + i * 16, 0, 16, 16);
    }

    @Override
    protected void gatherAdditionalTooltips(int mouseX, int mouseY, Consumer<Component> addLine, Consumer<Component> addGray) {
        super.gatherAdditionalTooltips(mouseX, mouseY, addLine, addGray);
        if (reset.isHovered() && menu.getCarried().isEmpty())
            addLine.accept(Component.translatable(Lib.GUI_CONFIG + "small_mining_machine.rest"));
    }

    @Override
    protected @NotNull List<InfoArea> makeInfoAreas() {
        return ImmutableList.of(
                new EnergyInfoArea(this.leftPos + 157, this.topPos + 21, this.menu.energyStorage)
        );
    }

    @Override
    protected void init() {
        super.init();
        reset = new GuiButtonIE(leftPos + 58, topPos + 50, 16, 16, Component.empty(), RESET,
                btn -> {
                    if (menu.getCarried().isEmpty()) {
                        CompoundTag compoundTag = new CompoundTag();
                        compoundTag.putInt("buttonId", 0);
                        sendUpdateToServer(compoundTag);
                    }
                }) {
            @Override
            public boolean isHoveredOrFocused() {
                return super.isHoveredOrFocused() && menu.getCarried().isEmpty();
            }
        };
        this.addRenderableWidget(reset);
    }
}
