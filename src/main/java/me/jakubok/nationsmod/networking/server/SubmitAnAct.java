package me.jakubok.nationsmod.networking.server;

import me.jakubok.nationsmod.administration.law.Act;
import me.jakubok.nationsmod.administration.law.Petition;
import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.administration.town.TownLawDescription;
import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.items.ActItem;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SubmitAnAct implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            ItemStack stack = player.getMainHandStack();
            if (!(stack.getItem() instanceof ActItem actItem)) {
                player.sendMessage(Text.of("Submitting failed! The held item is not a sealed act"));
                return;
            }

            Act<?> act = actItem.getTheAct(stack, server);
            if (act.description instanceof TownLawDescription) {
                Town town = (Town) act.getAffectedBody(server);
                @SuppressWarnings("unchecked")
                Petition<TownLawDescription> petition = new Petition<>((Act<TownLawDescription>)act, town.description);
                if (!town.isACitizen(server, new PlayerAccount(player))) {
                    player.sendMessage(Text.of("Submitting failed! You are not a citizen of the town of " + town.getName()));
                    return;
                }
                if (town.petitions.containsKey(petition.act.getId())) {
                    player.sendMessage(Text.of("Submitting failed! The same act is already submitted!"));
                    return;
                }
                town.petitions.put(petition.act.getId(), petition);
                player.setStackInHand(player.getActiveHand(), ItemStack.EMPTY);
                return;
            }
        });
    }
}
