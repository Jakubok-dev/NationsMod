package me.jakubok.nationsmod.networking.client;

import java.util.HashMap;
import java.util.Map;

import me.jakubok.nationsmod.NationsClient;
import me.jakubok.nationsmod.collection.Border;
import me.jakubok.nationsmod.collection.BorderSlots;
import me.jakubok.nationsmod.collection.Colour;
import me.jakubok.nationsmod.gui.BorderRegistratorScreen;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayChannelHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class OpenBorderRegistratorScreen implements PlayChannelHandler {

    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf,
            PacketSender responseSender) {
        
        BorderSlots slots = new BorderSlots(buf.readNbt());
        Map<String, Integer> slotMap = new HashMap<>();
        for (int i = 0; i < slots.slots.size(); i++)
            slotMap.put(slots.slots.get(i).name, i);

        client.execute(() -> {
            if (client.player.isSneaking()) {
                client.setScreen(new BorderRegistratorScreen(slotMap, client.currentScreen));
                return;
            }
    
            HitResult hit = client.crosshairTarget;
            BlockPos position = new BlockPos((int)hit.getPos().x, (int)hit.getPos().y, (int)hit.getPos().z);
    
            if (slots.getSelectedSlot() == null) {
                client.player.sendMessage(Text.translatable("gui.nationsmod.border_registrator.select_a_slot"), true);
                return;
            }
    
            if (slots.getSelectedSlot().contains(position)) {
    
                PacketByteBuf buffer = PacketByteBufs.create();
                buffer.writeBlockPos(slots.getSelectedSlot().get(position).position);
                ClientPlayNetworking.send(Packets.UNHIGHLIGHT_A_BLOCK_SERVER, buffer);
    
                NationsClient.drawer.unhighlightABlock(slots.getSelectedSlot().get(position).position, new Colour(255, 255, 255));
                NationsClient.map.clearTheBorderRegistratorLayer(slots.getSelectedSlot().get(position).position);
                NationsClient.borderSlot.delete(slots.getSelectedSlot().get(position).position.getX(), slots.getSelectedSlot().get(position).position.getZ());
    
                Text firstUnmarkMessage = Text.translatable("gui.nationsmod.border_registrator.unmark.1");
                Text secondUnmarkMessage = Text.translatable("gui.nationsmod.border_registrator.unmark.2");
                Text thirdUnmarkMessage = Text.translatable("gui.nationsmod.border_registrator.unmark.3");
    
                Text unmarkMessage = Text.of(
                    firstUnmarkMessage.getString() + " " +
                    "X:" + position.getX() + "; Z:" + position.getZ() + "; " +
                    secondUnmarkMessage.getString() + " " +
                    slots.getSelectedSlot().name + " " +
                    thirdUnmarkMessage.getString()
                );
    
                client.player.sendMessage(unmarkMessage, true);
    
            } else {
                Border border = new Border(position);
                NationsClient.drawer.highlightABlock(border.position, new Colour(255, 255, 255));
                NationsClient.map.renderTheBorderRegistratorLayer(border.position);
                NationsClient.borderSlot.insert(border);
                
                PacketByteBuf buffer = PacketByteBufs.create();
                buffer.writeBlockPos(border.position);
                ClientPlayNetworking.send(Packets.HIGHLIGHT_A_BLOCK_SERVER, buffer);
    
                Text firstMarkMessage = Text.translatable("gui.nationsmod.border_registrator.mark.1");
                Text secondMarkMessage = Text.translatable("gui.nationsmod.border_registrator.mark.2");
                Text thirdMarkMessage = Text.translatable("gui.nationsmod.border_registrator.mark.3");
    
                Text markMessage = Text.of(
                    firstMarkMessage.getString() + " " +
                    "X:" + position.getX() + "; Z:" + position.getZ() + "; " +
                    secondMarkMessage.getString() + " " +
                    slots.getSelectedSlot().name + " " +
                    thirdMarkMessage.getString()
                );
    
                client.player.sendMessage(markMessage, true);
            }
        });
    }
    
}
