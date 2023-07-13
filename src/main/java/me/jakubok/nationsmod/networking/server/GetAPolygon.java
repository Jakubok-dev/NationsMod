package me.jakubok.nationsmod.networking.server;

import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.collection.PlayerInfo;
import me.jakubok.nationsmod.geometry.Polygon;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class GetAPolygon implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int polygonIndex = buf.readInt();
        UUID packetID = buf.readUuid();
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeUuid(packetID);
        server.execute(() -> {
            if (polygonIndex == -1) {
                ServerPlayNetworking.send(player, Packets.OPEN_POLYGON_CREATION_SCREEN, PacketByteBufs.create());
                return;
            }
            PlayerInfo info = PlayerInfo.fromAccount(new PlayerAccount(player), server);
            Polygon polygon = info.polygonPlayerStorage.slots.get(polygonIndex);
            buffer.writeNbt(polygon.writeToNbtAndReturn(new NbtCompound()));
            buffer.writeBoolean(polygonIndex == info.polygonPlayerStorage.selectedSlot);
            ServerPlayNetworking.send(player, Packets.RECEIVE, buffer);
        });
    }
}
