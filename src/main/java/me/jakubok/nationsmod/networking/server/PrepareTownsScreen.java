package me.jakubok.nationsmod.networking.server;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import me.jakubok.nationsmod.administration.abstractEntities.LegalOrganisation;
import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.networking.Packets;
import me.jakubok.nationsmod.registries.LegalOrganisationRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayChannelHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class PrepareTownsScreen implements PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
            PacketByteBuf buf, PacketSender responseSender) {
        UUID id = buf.readUuid();
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeUuid(id);
        server.execute(() -> {
            NbtCompound compound = new NbtCompound();
            AtomicInteger size = new AtomicInteger(0);

            for (LegalOrganisation<?> town : LegalOrganisationRegistry.getRegistry(server).getOrganisations().values()) {
                if (!(town instanceof Town))
                    continue;
                compound.putString("town_name" + size.incrementAndGet(), town.getName());
                compound.putUuid("town_id" + size.get(), town.getId());
            }
            compound.putInt("size", size.get());

            buffer.writeNbt(compound);
            ServerPlayNetworking.send(player, Packets.RECEIVE, buffer);
        });
    }

    
}
