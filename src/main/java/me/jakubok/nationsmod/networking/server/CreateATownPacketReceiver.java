package me.jakubok.nationsmod.networking.server;

import java.util.concurrent.atomic.AtomicBoolean;

import me.jakubok.nationsmod.administration.Town;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayChannelHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

public class CreateATownPacketReceiver implements PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
        PacketByteBuf buf, PacketSender responseSender) {
        NbtCompound compound = buf.readNbt();
        String townName = compound.getString("town_name");
        String districtName = compound.getString("district_name");

        server.execute(() -> {
            // Check if the town name is unique
            AtomicBoolean unique = new AtomicBoolean(true);
            ComponentsRegistry.TOWNS_REGISTRY.get(server.getOverworld().getLevelProperties()).getTowns().forEach(el -> {
                if (townName.toLowerCase().equals(el.getName().toLowerCase())) {
                    player.sendMessage(new TranslatableText("gui.nationsmod.town_creation_screen.town_name_not_unique"), false);
                    unique.set(false);
                    return;
                }
            });

            if (!unique.get())
                return;

            if (!player.isCreative()) player.getMainHandStack().setCount(player.getMainHandStack().getCount()-1);

            new Town(townName, districtName, player.getChunkPos(), player.getEntityWorld());
        });
    }
}
