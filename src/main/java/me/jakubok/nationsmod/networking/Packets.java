package me.jakubok.nationsmod.networking;

import me.jakubok.nationsmod.NationsMod;
import net.minecraft.util.Identifier;

public class Packets {

    // Yes, it hurts my eyes too :)

    public static final Identifier OPEN_TOWNS_SCREEN_PACKET = new Identifier(NationsMod.MOD_ID, "open_towns_screen_packet");
    public static final Identifier PREPARE_TOWNS_SCREEN_PACKET = new Identifier(NationsMod.MOD_ID, "prepare_towns_screen_packet");

    public static final Identifier OPEN_TOWN_SCREEN_PACKET = new Identifier(NationsMod.MOD_ID, "open_town_screen_packet");
    public static final Identifier PREPARE_TOWN_SCREEN_PACKET = new Identifier(NationsMod.MOD_ID, "prepare_town_screen_packet");

    public static final Identifier OPEN_TOWN_CREATION_SCREEN_PACKET = new Identifier(NationsMod.MOD_ID, "open_town_creation_screen_packet");

    public static final Identifier CREATE_A_TOWN_PACKET = new Identifier(NationsMod.MOD_ID, "create_a_town_packet");

    public static final Identifier CHECK_POSITION = new Identifier(NationsMod.MOD_ID, "check_position_packet");

    public static final Identifier OPEN_NATION_CREATION_SCREEN_PACKET = new Identifier(NationsMod.MOD_ID, "open_nation_creation_screen_packet");

    public static final Identifier CREATE_A_NATION_PACKET = new Identifier(NationsMod.MOD_ID, "create_a_nation_packet");

    public static final Identifier OPEN_BORDER_REGISTRATOR_SCREEN_PACKET = new Identifier(NationsMod.MOD_ID, "open_border_registrator_screen_packet");
    
    public static final Identifier PREPARE_BORDER_SLOT_SCREEN_PACKET = new Identifier(NationsMod.MOD_ID, "prepare_border_slot_screen_packet");
    public static final Identifier OPEN_BORDER_SLOT_SCREEN_PACKET = new Identifier(NationsMod.MOD_ID, "open_border_slot_screen_packet");
    public static final Identifier OPEN_BORDER_SLOT_CREATOR_SCREEN_PACKET = new Identifier(NationsMod.MOD_ID, "open_border_slot_creator_screen_packet");

    public static final Identifier CREATE_A_BORDER_SLOT_PACKET = new Identifier(NationsMod.MOD_ID, "create_a_border_slot_packet");
    public static final Identifier DELETE_A_BORDER_SLOT_PACKET = new Identifier(NationsMod.MOD_ID, "delete_a_border_slot_packet");
    public static final Identifier SELECT_A_BORDER_SLOT_PACKET = new Identifier(NationsMod.MOD_ID, "select_a_border_slot_packet");
    public static final Identifier UNSELECT_A_BORDER_SLOT_PACKET = new Identifier(NationsMod.MOD_ID, "unselect_a_border_slot_packet");

    public static final Identifier HIGHLIGHT_A_BLOCK_PACKET = new Identifier(NationsMod.MOD_ID, "highlight_a_block_packet");
    public static final Identifier UNHIGHLIGHT_A_BLOCK_PACKET = new Identifier(NationsMod.MOD_ID, "unhighlight_a_block_packet");

    public static final Identifier REQUEST_A_CHUNK_REGISTRY = new Identifier(NationsMod.MOD_ID, "request_a_chunk_registry");
    public static final Identifier REQUEST_A_CHUNK_REGISTRY_RESPONSE = new Identifier(NationsMod.MOD_ID, "request_a_chunk_registry_response");
    
    public static final Identifier REQUEST_A_TERRITORY_CLAIMER = new Identifier(NationsMod.MOD_ID, "request_a_territory_claimer");
    public static final Identifier REQUEST_A_TERRITORY_CLAIMER_RESPONSE = new Identifier(NationsMod.MOD_ID, "request_a_territory_claimer_response");
}
