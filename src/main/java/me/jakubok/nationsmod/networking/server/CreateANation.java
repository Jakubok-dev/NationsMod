package me.jakubok.nationsmod.networking.server;

import java.util.concurrent.atomic.AtomicBoolean;

import me.jakubok.nationsmod.administration.District;
import me.jakubok.nationsmod.administration.Nation;
import me.jakubok.nationsmod.administration.TerritoryClaimer;
import me.jakubok.nationsmod.administration.Town;
import me.jakubok.nationsmod.chunk.ChunkClaimRegistry;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayChannelHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

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
            
            ComponentsRegistry.NATIONS_REGISTRY.get(player.getEntityWorld().getLevelProperties()).getNations().forEach(el -> {
                if (el.getName().toLowerCase().equals(nationName.toLowerCase())) {
                    validated.set(false);
                    player.sendMessage(new TranslatableText("gui.nationsmod.nation_creation_screen.nation_name_not_unique"), false);
                }
            });

            Town nationCapital = getTown(player.getEntityWorld(), player);

            if (nationCapital == null) {
                player.sendMessage(new TranslatableText("gui.nationsmod.nation_creation_screen.not_in_a_town"), false);
                validated.set(false);
                return;
            }

            if (!validated.get())
                return;

            new Nation(nationName, player.getEntityWorld(), provinceName, nationCapital);
        });
    }

    private Town getTown(World world, ServerPlayerEntity player) {
        ChunkClaimRegistry registry = ComponentsRegistry.CHUNK_BINARY_TREE.get(world).get(player.getBlockPos());

        if (registry == null)
            return null;

        if (!registry.isBelonging(player.getBlockPos()))
            return null;

        TerritoryClaimer<?> claimer = ComponentsRegistry.TERRITORY_CLAIMERS_REGISTRY.get(world.getLevelProperties()).getClaimer(registry.claimBelonging(player.getBlockPos()));

        if (!(claimer instanceof District))
            return null;

        return ((District)claimer).getTown();
    }
    
}
