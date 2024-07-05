package me.jakubok.nationsmod.networking.server;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.administration.abstractEntities.LegalOrganisation;
import me.jakubok.nationsmod.administration.abstractEntities.LegalOrganisationLawDescription;
import me.jakubok.nationsmod.administration.law.Act;
import me.jakubok.nationsmod.registries.LegalOrganisationRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.UUID;

public class CreateAnAct implements ServerPlayNetworking.PlayChannelHandler {
    @SuppressWarnings("unchecked")
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        NbtCompound nbt = buf.readNbt();
        if (nbt == null) {
            player.sendMessage(Text.of("ERROR! Packet has been lost"));
            return;
        }
        String name = nbt.getString("name");
        UUID bodyID = nbt.getUuid("bodyID");
        server.execute(() -> {
            LegalOrganisation<?> organisation = LegalOrganisationRegistry.getRegistry(server).get(bodyID);
            if (organisation == null) {
                player.sendMessage(Text.of("ERROR! Couldn't find the organisation"));
                return;
            }
            if (name == null) {
                player.sendMessage(Text.of("ERROR! Couldn't find the name"));
                return;
            }

            Act<?> act = new Act<>(name, organisation.description, (LegalOrganisation<LegalOrganisationLawDescription>)organisation);
            ItemStack stack = player.getStackInHand(player.preferredHand);
            stack.setCustomName(Text.literal(act.getName()).formatted(Formatting.ITALIC));
            NbtCompound stackNbt = stack.getOrCreateSubNbt(NationsMod.MOD_ID);
            stackNbt.putUuid("bodyID", bodyID);
            stackNbt.put("act", act.writeToNbtAndReturn(new NbtCompound()));
        });
    }
}
