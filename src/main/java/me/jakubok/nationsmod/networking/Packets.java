package me.jakubok.nationsmod.networking;

import me.jakubok.nationsmod.NationsMod;
import net.minecraft.util.Identifier;

public class Packets {
    public static final Identifier OPEN_TOWNS_SCREEN_PACKET = new Identifier(NationsMod.MOD_ID, "open_towns_screen_packet");
    public static final Identifier PREPARE_TOWNS_SCREEN_PACKET = new Identifier(NationsMod.MOD_ID, "prepare_towns_screen_packet");
    public static final Identifier OPEN_TOWN_CREATION_SCREEN_PACKET = new Identifier(NationsMod.MOD_ID, "open_town_creation_screen_packet");
    public static final Identifier CREATE_A_TOWN_PACKET = new Identifier(NationsMod.MOD_ID, "create_a_town_packet");
}
