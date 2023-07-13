package me.jakubok.nationsmod.networking.client;

import me.jakubok.nationsmod.collection.PolygonPlayerStorage;
import me.jakubok.nationsmod.gui.PolygonsStorageScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

import java.util.HashMap;
import java.util.Map;

public class OpenPolygonsStorageScreen implements ClientPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        NbtCompound nbt = buf.readNbt();
        Map<String, Integer> polygonNames = new HashMap<>();
        for (int i = 0; i < nbt.getInt("size"); i++)
            polygonNames.put(nbt.getString("polygon" + i), i);
        client.execute(() -> {
            client.setScreen(new PolygonsStorageScreen(polygonNames, client.currentScreen));
        });
    }
}
