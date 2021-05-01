package me.Jakubok.nations.gui;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;

public class TownCreationScreen extends CottonInventoryScreen<TownCreationDescription> {
    public TownCreationScreen(TownCreationDescription description, PlayerEntity player) {
        super(description, player);
    }
}
