package me.jakubok.nationsmod.items;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.administration.District;
import me.jakubok.nationsmod.administration.TerritoryClaimer;
import me.jakubok.nationsmod.chunk.ChunkClaimRegistry;
import me.jakubok.nationsmod.networking.Packets;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class NationIndependenceDeclaration extends Item implements Declaration {
    
    public NationIndependenceDeclaration() {
        super(
            new FabricItemSettings()
            .rarity(Rarity.RARE)
            .group(NationsMod.ITEM_GROUP)
            .maxCount(1)
        );
    }

    @Override
    public String type() {
        return "nation_independence";
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            ChunkClaimRegistry registry = ComponentsRegistry.CHUNK_BINARY_TREE.get(world).get(user.getBlockPos());

            if (registry == null) {
                user.sendMessage(new TranslatableText("gui.nationsmod.nation_creation_screen.not_in_a_town"), false);
                return super.use(world, user, hand);
            }

            if (!registry.isBelonging(user.getBlockPos())) {
                user.sendMessage(new TranslatableText("gui.nationsmod.nation_creation_screen.not_in_a_town"), false);
                return super.use(world, user, hand);
            }

            TerritoryClaimer claimer = ComponentsRegistry.TERRITORY_CLAIMERS_REGISTRY.get(world.getLevelProperties()).getClaimer(registry.claimBelonging(user.getBlockPos()));

            if (!(claimer instanceof District)) {
                user.sendMessage(new TranslatableText("gui.nationsmod.nation_creation_screen.not_in_a_town"), false);
                return super.use(world, user, hand);
            }

            ServerPlayNetworking.send((ServerPlayerEntity)user, Packets.OPEN_NATION_CREATION_SCREEN, PacketByteBufs.create());
        }
        return super.use(world, user, hand);
    }
}
