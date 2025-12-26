package com.chen1335.immersiveMechanical.client.gui;

import blusunrize.immersiveengineering.client.gui.TurretScreen;
import com.chen1335.immersiveMechanical.common.gui.LaserTurretMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class LaserTurretScreen extends TurretScreen<LaserTurretMenu> {
    public LaserTurretScreen(LaserTurretMenu container, Inventory inventoryPlayer, Component title) {
        super(container, inventoryPlayer, title);
    }

    @Override
    protected void addCustomButtons() {

    }
}
