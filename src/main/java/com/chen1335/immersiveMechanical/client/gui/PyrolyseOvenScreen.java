package com.chen1335.immersiveMechanical.client.gui;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.client.gui.IEContainerScreen;
import blusunrize.immersiveengineering.client.gui.info.EnergyInfoArea;
import blusunrize.immersiveengineering.client.gui.info.FluidInfoArea;
import blusunrize.immersiveengineering.client.gui.info.InfoArea;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.gui.PyrolyseOvenMenu;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

import static blusunrize.immersiveengineering.api.IEApi.ieLoc;

public class PyrolyseOvenScreen extends IEContainerScreen<PyrolyseOvenMenu> {
    private static final ResourceLocation TANK_OVERLAY = IEApi.ieLoc("cloche/tank_overlay");
    private static final ResourceLocation TEXTURE = ImmersiveMechanical.guiId("pyrolyse_oven");
    private static final ResourceLocation FLAME = ieLoc("coke_oven/flame");

    public PyrolyseOvenScreen(PyrolyseOvenMenu inventorySlotsIn, Inventory inv, Component title) {
        super(inventorySlotsIn, inv, title, TEXTURE);
        this.imageWidth = 174;
        this.imageHeight = 175;
        this.inventoryLabelY = 127;
        this.inventoryLabelX = 6;
    }

    @Override
    protected @NotNull List<InfoArea> makeInfoAreas() {
        return ImmutableList.of(
                new FluidInfoArea(this.menu.tank, new Rect2i(this.leftPos + 109, this.topPos + 16, 16, 47), 20, 51, TANK_OVERLAY),
                new EnergyInfoArea(this.leftPos + 155, this.topPos + 17, this.menu.energyStorage)
        );
    }

    @Override
    protected void drawContainerBackgroundPre(@Nonnull GuiGraphics graphics, float f, int mx, int my) {

        if (menu.process.get() > 0) {
            int h = (int) (12 * menu.process.get());
            graphics.blitSprite(FLAME, 9, 12, 0, 12 - h, leftPos + 50, topPos + 34 + 12 - h, 9, h);
        }
    }
}
