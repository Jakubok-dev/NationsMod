package me.jakubok.nationsmod.networking;

import me.jakubok.nationsmod.networking.server.CheckPosition;
import me.jakubok.nationsmod.networking.server.CreateANation;
import me.jakubok.nationsmod.networking.server.CreateATown;
import me.jakubok.nationsmod.networking.server.DeleteABorderSlot;
import me.jakubok.nationsmod.networking.server.CreateABorderSlot;
import me.jakubok.nationsmod.networking.server.PrepareBorderSlotScreen;
import me.jakubok.nationsmod.networking.server.PrepareTownScreen;
import me.jakubok.nationsmod.networking.server.PrepareTownsScreen;
import me.jakubok.nationsmod.networking.server.SelectABorderSlot;
import me.jakubok.nationsmod.networking.server.UnselectABorderSlot;
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
    }
}
