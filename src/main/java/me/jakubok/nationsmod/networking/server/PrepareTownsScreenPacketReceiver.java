package me.jakubok.nationsmod.networking.server;

import java.util.concurrent.atomic.AtomicInteger;

import me.jakubok.nationsmod.administration.Town;
import me.jakubok.nationsmod.networking.Packets;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayChannelHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class PrepareTownsScreenPacketReceiver implements PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
            PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            NbtCompound compound = new NbtCompound();
            AtomicInteger size = new AtomicInteger(0);

            for (Town town : ComponentsRegistry.TOWNS_REGISTRY.get(player.getEntityWorld().getLevelProperties()).getTowns()) {
                compound.putString("town_name" + size.incrementAndGet(), town.getName());
                compound.putUuid("town_id" + size.get(), town.getId());
            }
            compound.putInt("size", size.get());

            PacketByteBuf buffer = PacketByteBufs.create();
            buffer.writeNbt(compound);

            ServerPlayNetworking.send(player, Packets.OPEN_TOWNS_SCREEN_PACKET, buffer);
        });
    }

    
}
