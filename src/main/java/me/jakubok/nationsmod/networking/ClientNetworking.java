package me.jakubok.nationsmod.networking;

import me.jakubok.nationsmod.networking.client.HighlightABlockPacketReceiver;
import me.jakubok.nationsmod.networking.client.OpenBorderRegistratorScreenPacketReceiver;
import me.jakubok.nationsmod.networking.client.OpenBorderSlotCreatorScreenPacketReceiver;
import me.jakubok.nationsmod.networking.client.OpenBorderSlotScreenPacketReceiver;
import me.jakubok.nationsmod.networking.client.OpenNationCreationScreenPacketReceiver;
import me.jakubok.nationsmod.networking.client.OpenTownScreenPacketReceiver;
import me.jakubok.nationsmod.networking.client.OpenTownsScreenPacketReceiver;
import me.jakubok.nationsmod.networking.client.TownCreationScreenPacketReceiver;
import me.jakubok.nationsmod.networking.client.UnhighlightABlockPacketReceiver;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientNetworking {

    @Environment(EnvType.CLIENT)
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(Packets.OPEN_TOWN_CREATION_SCREEN_PACKET, new TownCreationScreenPacketReceiver());

        ClientPlayNetworking.registerGlobalReceiver(Packets.OPEN_TOWNS_SCREEN_PACKET, new OpenTownsScreenPacketReceiver());

        ClientPlayNetworking.registerGlobalReceiver(Packets.OPEN_TOWN_SCREEN_PACKET, new OpenTownScreenPacketReceiver());

        ClientPlayNetworking.registerGlobalReceiver(Packets.OPEN_NATION_CREATION_SCREEN_PACKET, new OpenNationCreationScreenPacketReceiver());

        ClientPlayNetworking.registerGlobalReceiver(Packets.OPEN_BORDER_REGISTRATOR_SCREEN_PACKET, new OpenBorderRegistratorScreenPacketReceiver());

        ClientPlayNetworking.registerGlobalReceiver(Packets.OPEN_BORDER_SLOT_CREATOR_SCREEN_PACKET, new OpenBorderSlotCreatorScreenPacketReceiver());

        ClientPlayNetworking.registerGlobalReceiver(Packets.OPEN_BORDER_SLOT_SCREEN_PACKET, new OpenBorderSlotScreenPacketReceiver());

        ClientPlayNetworking.registerGlobalReceiver(Packets.HIGHLIGHT_A_BLOCK_PACKET, new HighlightABlockPacketReceiver());

        ClientPlayNetworking.registerGlobalReceiver(Packets.UNHIGHLIGHT_A_BLOCK_PACKET, new UnhighlightABlockPacketReceiver());
    }
}
