package com.chen1335.immersiveMechanical.client.gui.info;

import blusunrize.immersiveengineering.client.gui.info.InfoArea;
import blusunrize.immersiveengineering.common.config.IEServerConfig;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Supplier;

public class GreenHouseFertilizerInfoArea extends InfoArea {
    private final Supplier<Integer> fertilizerAmount;
    private final Supplier<Float> fertilizerMod;

    public GreenHouseFertilizerInfoArea(int xMin, int yMin, Supplier<Integer> fertilizerAmount, Supplier<Float> fertilizerMod) {
        super(new Rect2i(xMin, yMin, 7, 48));
        this.fertilizerAmount = fertilizerAmount;
        this.fertilizerMod = fertilizerMod;
    }

    protected void fillTooltipOverArea(int mouseX, int mouseY, List<Component> tooltip) {
        tooltip.add(Component.translatable("desc.immersiveengineering.info.fertFill", new Object[]{Utils.formatDouble((double) ((float) (Integer) this.fertilizerAmount.get() / (float) (Integer) IEServerConfig.MACHINES.cloche_fertilizer.get()), "0.00")}));
        tooltip.add(Component.translatable("desc.immersiveengineering.info.fertMod", new Object[]{Utils.formatDouble((double) (Float) this.fertilizerMod.get(), "0.00")}));
    }

    public void draw(GuiGraphics graphics) {
        int height = this.area.getHeight();
        int stored = (int) ((float) height * ((float) this.fertilizerAmount.get() / (float) IEServerConfig.MACHINES.cloche_fertilizer.get())) / 3;
        graphics.fillGradient(this.area.getX(), this.area.getY() + (height - stored), this.area.getX() + this.area.getWidth(), this.area.getY() + this.area.getHeight(), -6951680, -7710208);
    }
}
