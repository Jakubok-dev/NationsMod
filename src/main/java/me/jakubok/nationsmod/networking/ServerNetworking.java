package me.jakubok.nationsmod.networking;

import me.jakubok.nationsmod.networking.server.CheckPositionPacketReceiver;
import me.jakubok.nationsmod.networking.server.CreateANationPacketReceiver;
import me.jakubok.nationsmod.networking.server.CreateATownPacketReceiver;
import me.jakubok.nationsmod.networking.server.DeleteABorderSlotPacketReceiver;
import me.jakubok.nationsmod.networking.server.CreateABorderSlotPacketReceiver;
import me.jakubok.nationsmod.networking.server.PrepareBorderSlotScreenPacketReceiver;
import me.jakubok.nationsmod.networking.server.PrepareTownScreenPacketReceiver;
import me.jakubok.nationsmod.networking.server.PrepareTownsScreenPacketReceiver;
import me.jakubok.nationsmod.networking.server.SelectABorderSlotPacketReceiver;
import me.jakubok.nationsmod.networking.server.UnselectABorderSlotPacketReceiver;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ServerNetworking {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(Packets.CREATE_A_TOWN_PACKET, new CreateATownPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(Packets.PREPARE_TOWNS_SCREEN_PACKET, new PrepareTownsScreenPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(Packets.CHECK_POSITION, new CheckPositionPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(Packets.PREPARE_TOWN_SCREEN_PACKET, new PrepareTownScreenPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(Packets.CREATE_A_NATION_PACKET, new CreateANationPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(Packets.PREPARE_BORDER_SLOT_SCREEN_PACKET, new PrepareBorderSlotScreenPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(Packets.CREATE_A_BORDER_SLOT_PACKET, new CreateABorderSlotPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(Packets.DELETE_A_BORDER_SLOT_PACKET, new DeleteABorderSlotPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(Packets.SELECT_A_BORDER_SLOT_PACKET, new SelectABorderSlotPacketReceiver());
        
        ServerPlayNetworking.registerGlobalReceiver(Packets.UNSELECT_A_BORDER_SLOT_PACKET, new UnselectABorderSlotPacketReceiver());
    }
}
