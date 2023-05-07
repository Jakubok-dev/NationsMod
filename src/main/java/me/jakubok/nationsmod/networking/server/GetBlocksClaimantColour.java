package me.jakubok.nationsmod.networking.server;

import java.util.UUID;

import me.jakubok.nationsmod.administration.district.District;
import me.jakubok.nationsmod.chunk.ChunkClaimRegistry;
import me.jakubok.nationsmod.collections.ChunkBinaryTree;
import me.jakubok.nationsmod.collections.Colour;
import me.jakubok.nationsmod.networking.Packets;
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
        PacketByteBuf responseBufferToRenderColour = PacketByteBufs.create();
        PacketByteBuf responseBufferToPushInfo = PacketByteBufs.create();
        responseBufferToRenderColour.writeBlockPos(pos);
        responseBufferToPushInfo.writeBlockPos(pos);

        server.execute(() -> {
            ChunkClaimRegistry registry = ChunkBinaryTree.getRegistry(player.getWorld()).get(pos);
            if (registry == null)
                return;
            
            UUID districtID = registry.claimBelonging(pos);
            if (districtID == null)
                return;

            District district = District.fromUUID(districtID, server);

            if (district == null)
                return;

            Colour colour = new Colour(district.getTheMapColour(server).getBitmask());
            responseBufferToRenderColour.writeInt(colour.getBitmask());

            ServerPlayNetworking.send(player, Packets.RENDER_CLAIMANTS_COLOUR, responseBufferToRenderColour);

            district.sendMapBlockInfo(player.getWorld(), pos);
        });
    }
}
