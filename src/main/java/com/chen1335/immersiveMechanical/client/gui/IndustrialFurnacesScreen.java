package com.chen1335.immersiveMechanical.client.gui;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.client.gui.IEContainerScreen;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import blusunrize.immersiveengineering.client.gui.info.EnergyInfoArea;
import blusunrize.immersiveengineering.client.gui.info.InfoArea;
import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.common.gui.IndustrialFurnacesMenu;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

import static blusunrize.immersiveengineering.api.IEApi.ieLoc;

public class IndustrialFurnacesScreen extends IEContainerScreen<IndustrialFurnacesMenu> {
    private static final ResourceLocation TEXTURE = ImmersiveMechanical.guiId("industrial_furnaces");
    private static final ResourceLocation PROGRESS = ieLoc("arc_furnace/progress");
    private static final GuiButtonIE.ButtonTexture DISTRIBUTE = new GuiButtonIE.ButtonTexture(
            ieLoc("arc_furnace/distribute"), ieLoc("arc_furnace/distribute_hover")
    );
    private GuiButtonIE distributeButton;

    public IndustrialFurnacesScreen(IndustrialFurnacesMenu inventorySlotsIn, Inventory inv, Component title) {
        super(inventorySlotsIn, inv, title, TEXTURE);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.inventoryLabelY = 127;
    }

    @Override
    protected @NotNull List<InfoArea> makeInfoAreas() {
        return ImmutableList.of(
                new EnergyInfoArea(this.leftPos + 157, this.topPos + 20, this.menu.energyStorage)
        );
    }

    @Override
    protected void gatherAdditionalTooltips(int mouseX, int mouseY, Consumer<Component> addLine, Consumer<Component> addGray)
    {
        super.gatherAdditionalTooltips(mouseX, mouseY, addLine, addGray);
        if(distributeButton.isHovered()&&menu.getCarried().isEmpty())
            addLine.accept(Component.translatable(Lib.GUI_CONFIG+"arcfurnace.distribute"));
    }

    @Override
    protected void drawContainerBackgroundPre(@Nonnull GuiGraphics graphics, float f, int mx, int my) {
        for (var process : menu.processes.get()) {
            int slot = process.slot();
            int h = (int) (process.progress() * 16);
            graphics.blitSprite(
                    PROGRESS, 3, 16, 0, 16 - h, leftPos + 27 + slot % 3 * 21, topPos + 18 + slot / 3 * 18 + (16 - h), 2, h
            );
        }
    }

    @Override
    protected void init() {
        super.init();
        super.init();
        distributeButton = new GuiButtonIE(leftPos+78, topPos+17, 16, 16, Component.empty(), DISTRIBUTE,
                btn -> {
                    if(menu.getCarried().isEmpty())
                        autoSplitStacks();
                })
        {
            @Override
            public boolean isHoveredOrFocused()
            {
                return super.isHoveredOrFocused()&&menu.getCarried().isEmpty();
            }
        };
        this.addRenderableWidget(distributeButton);
    }

    private void autoSplitStacks()
    {
        int emptySlot;
        int largestSlot;
        int largestCount;
        for(int j = 0; j < 9; j++)
        {
            emptySlot = -1;
            largestSlot = -1;
            largestCount = -1;
            for(int i = 0; i < 9; i++)
                if(menu.getSlot(i).hasItem())
                {
                    int count = menu.getSlot(i).getItem().getCount();
                    if(count > 1&&count > largestCount)
                    {
                        largestSlot = i;
                        largestCount = count;
                    }
                }
                else if(emptySlot < 0)
                    emptySlot = i;
            if(emptySlot >= 0&&largestSlot >= 0)
            {
                this.slotClicked(menu.getSlot(largestSlot), largestSlot, 1, ClickType.PICKUP);
                this.slotClicked(menu.getSlot(emptySlot), emptySlot, 0, ClickType.PICKUP);
            }
            else
                break;
        }
    }
}
