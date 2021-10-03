package me.jakubok.nationsmod.networking.client;

import java.util.HashMap;
import java.util.Map;

import me.jakubok.nationsmod.gui.BorderRegistratorScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayChannelHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class OpenBorderRegistratorScreenPacketReceiver implements PlayChannelHandler {

    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf,
            PacketSender responseSender) {
        
        NbtCompound tag = buf.readNbt();

        Map<String, Integer> slots = new HashMap<>();

        for (int i = 1; i <= tag.getInt("size"); i++) {
            slots.put(
                tag.getString("name" + i), 
                tag.getInt("index" + i)
            );
        }

        client.execute(() -> {
            client.setScreen(new BorderRegistratorScreen(slots));
        });
    }
    
}
