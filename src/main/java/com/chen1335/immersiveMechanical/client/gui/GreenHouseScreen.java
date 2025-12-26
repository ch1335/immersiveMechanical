package com.chen1335.immersiveMechanical.client.gui;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.client.gui.IEContainerScreen;
import blusunrize.immersiveengineering.client.gui.info.EnergyInfoArea;
import blusunrize.immersiveengineering.client.gui.info.FluidInfoArea;
import blusunrize.immersiveengineering.client.gui.info.InfoArea;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.client.gui.info.GreenHouseFertilizerInfoArea;
import com.chen1335.immersiveMechanical.common.gui.GreenHouseMenu;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GreenHouseScreen extends IEContainerScreen<GreenHouseMenu> {
    private static final ResourceLocation TEXTURE = ImmersiveMechanical.guiId("green_house");
    private static final ResourceLocation TANK_OVERLAY = IEApi.ieLoc("cloche/tank_overlay");

    private static final ResourceLocation PROGRESS = ImmersiveMechanical.id("green_house/progress");

    public GreenHouseScreen(GreenHouseMenu inventorySlotsIn, Inventory inv, Component title) {
        super(inventorySlotsIn, inv, title, TEXTURE);
        this.imageWidth = 236;
        this.imageHeight = 202;
        this.inventoryLabelY = 127;
        this.inventoryLabelX = 39;
    }

    @Override
    protected @NotNull List<InfoArea> makeInfoAreas() {
        return ImmutableList.of(
                new FluidInfoArea(this.menu.tank, new Rect2i(this.leftPos + 9, this.topPos + 23, 16, 47), 20, 51, TANK_OVERLAY),
                new EnergyInfoArea(this.leftPos + 217, this.topPos + 25, this.menu.energyStorage),
                new GreenHouseFertilizerInfoArea(this.leftPos + 31, this.topPos + 22, this.menu.fertilizerAmount, this.menu.fertilizerMod)
        );
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
    }


    @Override
    protected void drawContainerBackgroundPre(@NotNull GuiGraphics graphics, float partialTicks, int x, int y) {
        super.drawContainerBackgroundPre(graphics, partialTicks, x, y);
        graphics.pose().pushPose();
        menu.processInfo.forEach((id, getterAndSetter) -> {
            renderUnitProcess(graphics, id, getterAndSetter.get());
        });

        graphics.pose().popPose();
    }

    private void renderUnitProcess(GuiGraphics graphics, int slot, float process) {
        int h = (int) (process * 16.0F);
        graphics.blitSprite(ImmersiveMechanical.id("green_house/progress"), 3, 16, 0, 16 - h, this.leftPos + 60 + slot / 4 * 21, this.topPos + 10 + slot % 4 * 18 + (16 - h), 2, h);

    }
}
