package me.jakubok.nationsmod.networking.server;

import java.util.concurrent.atomic.AtomicBoolean;

import me.jakubok.nationsmod.administration.abstractEntities.TerritoryClaimer;
import me.jakubok.nationsmod.administration.district.District;
import me.jakubok.nationsmod.administration.nation.Nation;
import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.chunk.ChunkClaimRegistry;
import me.jakubok.nationsmod.collection.ChunkBinaryTree;
import me.jakubok.nationsmod.registries.LegalOrganisationRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayChannelHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class CreateANation implements PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
            PacketByteBuf buf, PacketSender responseSender) {
        
        NbtCompound compound = buf.readNbt();
        String nationName = compound.getString("nation_name");
        String provinceName = compound.getString("province_name");

        server.execute(() -> {
            // Check if the nation name is unique and if the town belongs to a province
            AtomicBoolean validated = new AtomicBoolean(true);
            
            LegalOrganisationRegistry.getRegistry(server).getOrganisations().values().forEach(el -> {
                if (!(el instanceof Nation))
                    return;
                if (el.getName().toLowerCase().equals(nationName.toLowerCase())) {
                    validated.set(false);
                    player.sendMessage(Text.translatable("gui.nationsmod.nation_creation_screen.nation_name_not_unique"), false);
                }
            });

            Town nationCapital = getTown(player.getWorld(), player);

            if (nationCapital == null) {
                player.sendMessage(Text.translatable("gui.nationsmod.nation_creation_screen.not_in_a_town"), false);
                validated.set(false);
                return;
            }

            if (!validated.get())
                return;

            new Nation(nationName, provinceName, nationCapital, server);
        });
    }

    private Town getTown(ServerWorld world, ServerPlayerEntity player) {
        ChunkClaimRegistry registry = ChunkBinaryTree.getRegistry(world).get(player.getBlockPos());

        if (registry == null)
            return null;

        if (!registry.isBelonging(player.getBlockPos()))
            return null;

        TerritoryClaimer<?> claimer = (TerritoryClaimer<?>)LegalOrganisationRegistry.getRegistry(player.getServer()).get(registry.claimBelonging(player.getBlockPos()));

        if (!(claimer instanceof District))
            return null;

        return ((District)claimer).getTown(player.getServer());
    }
    
}
