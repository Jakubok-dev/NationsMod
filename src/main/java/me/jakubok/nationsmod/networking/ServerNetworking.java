package me.jakubok.nationsmod.networking;

import me.jakubok.nationsmod.networking.server.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ServerNetworking {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(Packets.CREATE_A_TOWN, new CreateATown());
        ServerPlayNetworking.registerGlobalReceiver(Packets.PREPARE_TOWNS_SCREEN, new PrepareTownsScreen());
        ServerPlayNetworking.registerGlobalReceiver(Packets.CHECK_POSITION, new CheckPosition());
        ServerPlayNetworking.registerGlobalReceiver(Packets.PREPARE_TOWN_SCREEN, new PrepareTownScreen());
        ServerPlayNetworking.registerGlobalReceiver(Packets.CREATE_A_NATION, new CreateANation());
        ServerPlayNetworking.registerGlobalReceiver(Packets.PREPARE_BORDER_SLOT_SCREEN, new PrepareBorderSlotScreen());
        ServerPlayNetworking.registerGlobalReceiver(Packets.CREATE_A_BORDER_SLOT, new CreateABorderSlot());
        ServerPlayNetworking.registerGlobalReceiver(Packets.DELETE_A_BORDER_SLOT, new DeleteABorderSlot());
        ServerPlayNetworking.registerGlobalReceiver(Packets.SELECT_A_BORDER_SLOT, new SelectABorderSlot());
        ServerPlayNetworking.registerGlobalReceiver(Packets.UNSELECT_A_BORDER_SLOT, new UnselectABorderSlot());
        ServerPlayNetworking.registerGlobalReceiver(Packets.GET_BLOCKS_CLAIMANT_COLOUR, new GetBlocksClaimantColour());
        ServerPlayNetworking.registerGlobalReceiver(Packets.HIGHLIGHT_A_BLOCK_SERVER, new HighlightABlock());
        ServerPlayNetworking.registerGlobalReceiver(Packets.UNHIGHLIGHT_A_BLOCK_SERVER, new UnhighlightABlock());
        ServerPlayNetworking.registerGlobalReceiver(Packets.PREPARE_BORDER_REGISTRATOR_SCREEN, new PrepareBorderRegistratorScreen());
        ServerPlayNetworking.registerGlobalReceiver(Packets.GET_A_NATION, new GetANation());
        ServerPlayNetworking.registerGlobalReceiver(Packets.GET_A_PROVINCE, new GetAProvince());
        ServerPlayNetworking.registerGlobalReceiver(Packets.SELECT_A_POLYGON, new SelectAPolygon());
        ServerPlayNetworking.registerGlobalReceiver(Packets.UNSELECT_A_POLYGON, new UnselectAPolygon());
        ServerPlayNetworking.registerGlobalReceiver(Packets.REMOVE_A_POLYGON, new RemoveAPolygon());
        ServerPlayNetworking.registerGlobalReceiver(Packets.GET_A_POLYGON, new GetAPolygon());
        ServerPlayNetworking.registerGlobalReceiver(Packets.CREATE_A_POLYGON, new CreateAPolygon());
        ServerPlayNetworking.registerGlobalReceiver(Packets.BORDER_REGISTRATOR_CLICKED, new BorderRegistratorClicked());
        ServerPlayNetworking.registerGlobalReceiver(Packets.CHANGE_THE_POLYGON_ALTERATION_MODE, new ChangeThePolygonAlterationMode());
    }
}
