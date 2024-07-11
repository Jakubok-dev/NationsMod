package me.jakubok.nationsmod.networking.server;

import me.jakubok.nationsmod.collection.Pair;
import me.jakubok.nationsmod.networking.Packets;
import me.jakubok.nationsmod.registries.LegalOrganisationRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GetDistrictsNames implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        NbtCompound nbt = buf.readNbt();
        UUID packetID = buf.readUuid();
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeUuid(packetID);
        List<UUID> districtsIDs = new ArrayList<>();
        for (int i = 0; i < nbt.getInt("size"); i++)
            districtsIDs.add(nbt.getUuid("id" + i));
        server.execute(() -> {
            Map<String, UUID> districts = new HashMap<>();
            districtsIDs.stream().forEach(e -> {
                String name = LegalOrganisationRegistry.getRegistry(server).get(e).getName();
                districts.put(name, e);

                NbtCompound outputNbt = new NbtCompound();
                AtomicInteger size = new AtomicInteger(0);
                for (String n : districts.keySet()) {
                    outputNbt.putString("key" + size.get(), n);
                    outputNbt.putUuid("value" + size.getAndIncrement(), districts.get(n));
                }
                outputNbt.putInt("size", size.get());
                buffer.writeNbt(outputNbt);
                ServerPlayNetworking.send(player, Packets.RECEIVE, buffer);
            });
        });
    }
}
