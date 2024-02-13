package me.jakubok.nationsmod.networking;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.jakubok.nationsmod.networking.client.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayChannelHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ClientNetworking {

    static Map<UUID, PlayChannelHandler> SERVER_REQUESTS = new HashMap<>();

    public static void callTheResponse(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf,
    PacketSender responseSender) {
        UUID id = buf.readUuid();
        SERVER_REQUESTS.get(id).receive(client, handler, buf, responseSender);
        SERVER_REQUESTS.remove(id);
    }

    public static boolean makeARequest(Identifier packet, PacketByteBuf buffer, PlayChannelHandler responseFunction) {
        UUID id = UUID.randomUUID();
        SERVER_REQUESTS.put(id, responseFunction);
        buffer.writeUuid(id);
        ClientPlayNetworking.send(packet, buffer);
        return true;
    }

    @Environment(EnvType.CLIENT)
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(Packets.OPEN_TOWN_CREATION_SCREEN, new OpenTownCreationScreen());
        ClientPlayNetworking.registerGlobalReceiver(Packets.OPEN_NATION_CREATION_SCREEN, new OpenNationCreationScreen());
        ClientPlayNetworking.registerGlobalReceiver(Packets.OPEN_BORDER_REGISTRATOR_SCREEN, new OpenBorderRegistratorScreen());
        ClientPlayNetworking.registerGlobalReceiver(Packets.OPEN_BORDER_SLOT_CREATOR_SCREEN, new OpenBorderSlotCreatorScreen());
        ClientPlayNetworking.registerGlobalReceiver(Packets.RECEIVE, new Receive());
        ClientPlayNetworking.registerGlobalReceiver(Packets.RENDER_CLAIMANTS_COLOUR, new RenderClaimantsColour());
        ClientPlayNetworking.registerGlobalReceiver(Packets.HIGHLIGHT_A_BLOCK_CLIENT, new HighlightABlock());
        ClientPlayNetworking.registerGlobalReceiver(Packets.UNHIGHLIGHT_A_BLOCK_CLIENT, new UnhighlightABlock());
        ClientPlayNetworking.registerGlobalReceiver(Packets.CLEAR_CLAIMANT_ON_THE_MAP, new ClearClaimantOnTheMap());
        ClientPlayNetworking.registerGlobalReceiver(Packets.PULL_MAP_BLOCK_INFO, new PullMapBlockInfo());
        ClientPlayNetworking.registerGlobalReceiver(Packets.OPEN_POLYGONS_STORAGE_SCREEN, new OpenPolygonsStorageScreen());
        ClientPlayNetworking.registerGlobalReceiver(Packets.OPEN_POLYGON_CREATION_SCREEN, new OpenPolygonCreationScreen());
        ClientPlayNetworking.registerGlobalReceiver(Packets.CACHE_A_POLYGON, new CacheAPolygon());
        ClientPlayNetworking.registerGlobalReceiver(Packets.UNCACHE_A_POLYGON, new UncacheAPolygon());
    }
}
