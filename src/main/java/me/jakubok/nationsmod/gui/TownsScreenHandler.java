package me.jakubok.nationsmod.gui;

import me.jakubok.nationsmod.registries.ModScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;

public class TownsScreenHandler extends ScreenHandler {

    public TownsScreenHandler(int syncId, PlayerInventory inv) {
        super(ModScreenHandlerRegistry.TOWNS_SCREEN_HANDLER, syncId);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
    
}
