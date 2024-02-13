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

import java.util.function.Consumer;

public class SelectAPolygon implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int polygonIndex = buf.readInt();
        server.execute(() -> {
            PlayerInfo info = PlayerInfo.fromAccount(new PlayerAccount(player), server);
            if (info.polygonPlayerStorage.selectedSlot != -1)
                info.polygonPlayerStorage.getSelectedPolygon().unsubscribe("client" + player.getUuid().toString());
            info.polygonPlayerStorage.selectedSlot = polygonIndex;

            Consumer<Polygon> subscriber = p -> {
                PacketByteBuf buffer = PacketByteBufs.create();
                buffer.writeNbt(p.writeToNbtAndReturn(new NbtCompound()));
                ServerPlayNetworking.send(player, Packets.CACHE_A_POLYGON, buffer);
            };
            info.polygonPlayerStorage.getSelectedPolygon().subscribe("client" + player.getUuid().toString(), subscriber);
            subscriber.accept(info.polygonPlayerStorage.getSelectedPolygon());
        });
    }
}
