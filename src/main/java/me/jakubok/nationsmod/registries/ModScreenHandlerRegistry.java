package me.jakubok.nationsmod.registries;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.gui.TownsScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlerRegistry {

    public static final ScreenHandlerType<TownsScreenHandler> TOWNS_SCREEN_HANDLER;
    
    static {
        TOWNS_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(NationsMod.MOD_ID, "towns_screen_handler"), TownsScreenHandler::new);
    }
    
    public static void init() {}
}
