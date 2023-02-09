package me.jakubok.nationsmod.networking.server;

import java.util.UUID;

import me.jakubok.nationsmod.administration.District;
import me.jakubok.nationsmod.chunk.ChunkClaimRegistry;
import me.jakubok.nationsmod.collections.Colour;
import me.jakubok.nationsmod.networking.Packets;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayChannelHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class GetBlocksClaimantColour implements PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
            PacketByteBuf buf, PacketSender responseSender) {
        BlockPos pos = buf.readBlockPos();
        PacketByteBuf responseBuffer = PacketByteBufs.create();
        responseBuffer.writeBlockPos(pos);

        server.execute(() -> {
            ChunkClaimRegistry registry = ComponentsRegistry.CHUNK_BINARY_TREE.get(player.getEntityWorld()).get(pos);
            if (registry == null) {
                responseBuffer.writeInt(-1);
                ServerPlayNetworking.send(player, Packets.RENDER_CLAIMANTS_COLOUR, responseBuffer);
                return;
            }
            
            UUID districtID = registry.claimBelonging(pos);
            if (districtID == null) {
                responseBuffer.writeInt(-1);
                ServerPlayNetworking.send(player, Packets.RENDER_CLAIMANTS_COLOUR, responseBuffer);
                return;
            }

            District district = District.fromUUID(districtID, player.getEntityWorld());
            Colour colour = new Colour(district.mapColour.getBitmask());
            responseBuffer.writeInt(colour.getBitmask());

            ServerPlayNetworking.send(player, Packets.RENDER_CLAIMANTS_COLOUR, responseBuffer);
        });
    }
}
