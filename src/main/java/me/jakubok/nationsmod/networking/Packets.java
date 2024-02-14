package me.jakubok.nationsmod.networking;

import me.jakubok.nationsmod.NationsMod;
import net.minecraft.util.Identifier;

public class Packets {

    // Yes, it hurts my eyes too :)

    public static final Identifier PREPARE_TOWNS_SCREEN = new Identifier(NationsMod.MOD_ID, "prepare_towns_screen");
    public static final Identifier PREPARE_TOWN_SCREEN = new Identifier(NationsMod.MOD_ID, "prepare_town_screen");

    public static final Identifier OPEN_TOWN_CREATION_SCREEN = new Identifier(NationsMod.MOD_ID, "open_town_creation_screen");

    public static final Identifier CREATE_A_TOWN = new Identifier(NationsMod.MOD_ID, "create_a_town");

    public static final Identifier CHECK_POSITION = new Identifier(NationsMod.MOD_ID, "check_position");

    public static final Identifier OPEN_NATION_CREATION_SCREEN = new Identifier(NationsMod.MOD_ID, "open_nation_creation_screen");

    public static final Identifier CREATE_A_NATION = new Identifier(NationsMod.MOD_ID, "create_a_nation");

    public static final Identifier OPEN_BORDER_REGISTRATOR_SCREEN = new Identifier(NationsMod.MOD_ID, "open_border_registrator_screen");
    
    public static final Identifier PREPARE_BORDER_SLOT_SCREEN = new Identifier(NationsMod.MOD_ID, "prepare_border_slot_screen");
    public static final Identifier OPEN_BORDER_SLOT_CREATOR_SCREEN = new Identifier(NationsMod.MOD_ID, "open_border_slot_creator_screen");

    public static final Identifier CREATE_A_BORDER_SLOT = new Identifier(NationsMod.MOD_ID, "create_a_border_slot");
    public static final Identifier DELETE_A_BORDER_SLOT = new Identifier(NationsMod.MOD_ID, "delete_a_border_slot");
    public static final Identifier SELECT_A_BORDER_SLOT = new Identifier(NationsMod.MOD_ID, "select_a_border_slot");
    public static final Identifier UNSELECT_A_BORDER_SLOT = new Identifier(NationsMod.MOD_ID, "unselect_a_border_slot");

    public static final Identifier HIGHLIGHT_A_BLOCK_CLIENT = new Identifier(NationsMod.MOD_ID, "highlight_a_block_client");
    public static final Identifier UNHIGHLIGHT_A_BLOCK_CLIENT = new Identifier(NationsMod.MOD_ID, "unhighlight_a_block_client");

    public static final Identifier HIGHLIGHT_A_BLOCK_SERVER = new Identifier(NationsMod.MOD_ID, "highlight_a_block_server");
    public static final Identifier UNHIGHLIGHT_A_BLOCK_SERVER = new Identifier(NationsMod.MOD_ID, "unhighlight_a_block_server");

    public static final Identifier RECEIVE = new Identifier(NationsMod.MOD_ID, "receive");

    public static final Identifier GET_BLOCKS_CLAIMANT_COLOUR = new Identifier(NationsMod.MOD_ID, "get_chunk_claimants_colour");
    public static final Identifier RENDER_CLAIMANTS_COLOUR = new Identifier(NationsMod.MOD_ID, "render_claimants_colour");
    public static final Identifier CLEAR_CLAIMANT_ON_THE_MAP = new Identifier(NationsMod.MOD_ID, "clear_claimant_on_the_map");
    public static final Identifier PULL_MAP_BLOCK_INFO = new Identifier(NationsMod.MOD_ID, "pull_map_block_info");

    public static final Identifier PREPARE_BORDER_REGISTRATOR_SCREEN = new Identifier(NationsMod.MOD_ID, "prepare_border_registrator_screen");

    public static final Identifier GET_A_NATION = new Identifier(NationsMod.MOD_ID, "get_a_nation");
    public static final Identifier GET_A_PROVINCE = new Identifier(NationsMod.MOD_ID, "get_a_province");

    public static final Identifier OPEN_POLYGONS_STORAGE_SCREEN = new Identifier(NationsMod.MOD_ID, "open_polygons_storage_screen");
    public static final Identifier SELECT_A_POLYGON = new Identifier(NationsMod.MOD_ID, "select_a_polygon");
    public static final Identifier UNSELECT_A_POLYGON = new Identifier(NationsMod.MOD_ID, "unselect_a_polygon");
    public static final Identifier REMOVE_A_POLYGON = new Identifier(NationsMod.MOD_ID, "remove_a_polygon");
    public static final Identifier GET_A_POLYGON = new Identifier(NationsMod.MOD_ID, "get_a_polygon");
    public static final Identifier OPEN_POLYGON_CREATION_SCREEN = new Identifier(NationsMod.MOD_ID, "open_polygon_creation_screen");
    public static final Identifier CREATE_A_POLYGON = new Identifier(NationsMod.MOD_ID, "create_a_polygon");

    public static final Identifier BORDER_REGISTRATOR_CLICKED = new Identifier(NationsMod.MOD_ID, "border_registrator_clicked");
    public static final Identifier CACHE_A_POLYGON = new Identifier(NationsMod.MOD_ID, "cache_a_polygon");
    public static final Identifier UNCACHE_A_POLYGON = new Identifier(NationsMod.MOD_ID, "uncache_a_polygon");
    public static final Identifier CHANGE_THE_POLYGON_ALTERATION_MODE = new Identifier(NationsMod.MOD_ID, "change_the_polygon_alteration_mode");
}
