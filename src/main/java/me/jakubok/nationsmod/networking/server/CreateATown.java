package me.jakubok.nationsmod.networking.server;

import java.util.concurrent.atomic.AtomicBoolean;

import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.collection.PlayerInfo;
import me.jakubok.nationsmod.registries.LegalOrganisationRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayChannelHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class CreateATown implements PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
        PacketByteBuf buf, PacketSender responseSender) {
        NbtCompound compound = buf.readNbt();
        String townName = compound.getString("town_name");
        String districtName = compound.getString("district_name");

        server.execute(() -> {
            // Check if the town name is unique
            AtomicBoolean unique = new AtomicBoolean(true);
            LegalOrganisationRegistry.getRegistry(server).getOrganisations().values().forEach(el -> {
                if (!(el instanceof Town))
                    return;
                if (townName.toLowerCase().equals(el.getName().toLowerCase())) {
                    player.sendMessage(Text.translatable("gui.nationsmod.town_creation_screen.town_name_not_unique"), false);
                    unique.set(false);
                    return;
                }
            });

            if (!unique.get())
                return;

            if (!player.isCreative()) player.getMainHandStack().setCount(player.getMainHandStack().getCount()-1);

            Town town = new Town(townName, districtName, player.getChunkPos(), player.getWorld(), PlayerInfo.fromAccount(new PlayerAccount(player), server).slots.getSelectedSlot(), server);
            town.addAMember(player, server);
        });
    }
}
