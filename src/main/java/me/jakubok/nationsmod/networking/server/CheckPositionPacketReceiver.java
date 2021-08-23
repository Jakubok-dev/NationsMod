package me.jakubok.nationsmod.networking.server;

import java.util.UUID;

import me.jakubok.nationsmod.administration.District;
import me.jakubok.nationsmod.administration.TerritoryClaimer;
import me.jakubok.nationsmod.chunk.ChunkClaimRegistry;
import me.jakubok.nationsmod.collections.PlayerInfo;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayChannelHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class CheckPositionPacketReceiver implements PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
            PacketByteBuf buf, PacketSender responseSender) {
        ChunkClaimRegistry chunkClaimRegistry = ComponentsRegistry.CHUNK_BINARY_TREE.get(player.getEntityWorld().getLevelProperties()).get(player.getBlockPos());

        PlayerInfo info = ComponentsRegistry.PLAYER_INFO.get(player);

        if (chunkClaimRegistry == null) {
            wilderness(info, player);
            return;
        }

        if (chunkClaimRegistry.claimBelonging(player.getBlockPos()) != null) {
            if (!info.inAWilderness)
                return;
            info.inAWilderness = false;

            UUID territoryClaimerID = chunkClaimRegistry.claimBelonging(player.getBlockPos());
            TerritoryClaimer territoryClaimer = ComponentsRegistry.TERRITORY_CLAIMERS_REGISTRY.get(player.getEntityWorld().getLevelProperties()).getClaimer(territoryClaimerID); 
            Text.of("" + territoryClaimer);

            if (territoryClaimer instanceof District) {
                District district = (District)territoryClaimer;
                player.sendMessage(Text.of(district.getTown().getName() + " | " + district.getName()), true);
            }
            return;
        }
        wilderness(info, player);
    }

    private void wilderness(PlayerInfo info, ServerPlayerEntity player) {
        if (info.inAWilderness)
            return;
        info.inAWilderness = true;
        player.sendMessage(new TranslatableText("nationsmod.wilderness"), true);
    }
    
}
