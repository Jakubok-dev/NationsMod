package me.jakubok.nationsmod.networking.client;

import me.jakubok.nationsmod.NationsClient;
import me.jakubok.nationsmod.geometry.Polygon;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class CacheAPolygon implements ClientPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        NbtCompound nbt = buf.readNbt();
        Polygon polygon = new Polygon(nbt);
        client.execute(() -> {
            NationsClient.polygonDrawer.addAPolygon(polygon);
        });
    }
}
