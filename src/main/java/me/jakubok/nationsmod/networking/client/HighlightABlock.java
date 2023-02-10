package me.jakubok.nationsmod.networking.client;

import me.jakubok.nationsmod.NationsClient;
import me.jakubok.nationsmod.collections.Colour;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayChannelHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class HighlightABlock implements PlayChannelHandler {

    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        BlockPos pos = buf.readBlockPos();
        Colour colour = new Colour(buf.readInt());
        boolean renderOnMap = buf.readBoolean();

        client.execute(() -> {
            NationsClient.drawer.highLightABlock(pos, colour);
            if (renderOnMap)
                NationsClient.map.renderTheBorderRegistratorLayer(pos);
        });
    }
    
}
