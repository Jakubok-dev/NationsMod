package me.jakubok.nationsmod.networking.client;

import java.util.UUID;

import me.jakubok.nationsmod.NationsClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayChannelHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class PullMapBlockInfo implements PlayChannelHandler {

    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf,
            PacketSender responseSender) {
        BlockPos pos = buf.readBlockPos();
        NbtCompound nbt = buf.readNbt();
        String townsName = nbt.getString("townsName");
        UUID townsUUID = nbt.getUuid("townsUUID");
        String districtsName = nbt.getString("districtsName");
        UUID districtsUUID = nbt.getUuid("districtsUUID");
        client.execute(() -> {
            if (townsName != null && townsUUID != null)
                NationsClient.map.addATownsName(townsName, townsUUID, pos);
            if (districtsName != null && districtsUUID != null) 
                NationsClient.map.addADistrictsName(districtsName, districtsUUID, pos);
        });
    }
    
}
