package me.jakubok.nationsmod.networking.server;

import java.util.concurrent.atomic.AtomicInteger;

import me.jakubok.nationsmod.administration.Town;
import me.jakubok.nationsmod.networking.Packets;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class PrepareTownsScreenPacketReceiver {

    public static void handle(MinecraftServer minecraftServer, ServerPlayerEntity playerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        minecraftServer.execute(() -> {
            NbtCompound compound = new NbtCompound();
            AtomicInteger size = new AtomicInteger(0);

            for (Town town : ComponentsRegistry.TOWNS_REGISTRY.get(playerEntity.getEntityWorld().getLevelProperties()).getTowns()) {
                compound.putString("town_name" + size.incrementAndGet(), town.getName());
                compound.putUuid("town_id" + size.get(), town.getId());
            }
            compound.putInt("size", size.get());

            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeNbt(compound);

            ServerPlayNetworking.send(playerEntity, Packets.OPEN_TOWNS_SCREEN_PACKET, buf);
        });
    }

    
}
