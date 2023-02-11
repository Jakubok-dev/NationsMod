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
import net.minecraft.nbt.NbtCompound;
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
            ChunkClaimRegistry registry = ComponentsRegistry.CHUNK_BINARY_TREE.get(player.getEntityWorld()).get(pos);
            if (registry == null)
                return;
            
            UUID districtID = registry.claimBelonging(pos);
            if (districtID == null)
                return;

            District district = District.fromUUID(districtID, player.getEntityWorld());

            if (district == null)
                return;

            Colour colour = new Colour(district.mapColour.getBitmask());
            responseBufferToRenderColour.writeInt(colour.getBitmask());

            ServerPlayNetworking.send(player, Packets.RENDER_CLAIMANTS_COLOUR, responseBufferToRenderColour);

            NbtCompound nbt = new NbtCompound();
            nbt.putString("townsName", district.getTown().getName());
            nbt.putString("districtsName", district.getName());
            nbt.putUuid("townsUUID", district.getTown().getId());
            nbt.putUuid("districtsUUID", districtID);
            responseBufferToPushInfo.writeNbt(nbt);

            ServerPlayNetworking.send(player, Packets.PULL_MAP_BLOCK_INFO, responseBufferToPushInfo);
        });
    }
}
