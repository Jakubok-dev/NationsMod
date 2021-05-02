package me.Jakubok.nations.util;

import me.Jakubok.nations.Nations;
import me.Jakubok.nations.gui.TownCreationDescription;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class GUIs {

    public static ScreenHandlerType TOWN_CREATION;

    public static void init() {
        TOWN_CREATION = ScreenHandlerRegistry.registerSimple(new Identifier(Nations.MOD_ID, "town_creation_gui"), (syncId, inventory) -> new TownCreationDescription(syncId, inventory, ScreenHandlerContext.EMPTY));
    }
}
