package me.jakubok.nationsmod.items;

import me.jakubok.nationsmod.administration.abstractEntities.TerritoryClaimer;
import me.jakubok.nationsmod.administration.district.District;
import me.jakubok.nationsmod.chunk.ChunkClaimRegistry;
import me.jakubok.nationsmod.collection.ChunkBinaryTree;
import me.jakubok.nationsmod.networking.Packets;
import me.jakubok.nationsmod.registries.LegalOrganisationRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class NationIndependenceDeclaration extends Item implements Declaration {
    
    public NationIndependenceDeclaration() {
        super(
            new FabricItemSettings()
            .rarity(Rarity.RARE)
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
            ChunkClaimRegistry registry = ChunkBinaryTree.getRegistry((ServerWorld)world).get(user.getBlockPos());

            if (registry == null) {
                user.sendMessage(Text.translatable("gui.nationsmod.nation_creation_screen.not_in_a_town"), false);
                return super.use(world, user, hand);
            }

            if (!registry.isBelonging(user.getBlockPos())) {
                user.sendMessage(Text.translatable("gui.nationsmod.nation_creation_screen.not_in_a_town"), false);
                return super.use(world, user, hand);
            }

            TerritoryClaimer<?> claimer = (TerritoryClaimer<?>)LegalOrganisationRegistry.getRegistry(((ServerWorld)world).getServer()).get(registry.claimBelonging(user.getBlockPos()));

            if (!(claimer instanceof District)) {
                user.sendMessage(Text.translatable("gui.nationsmod.nation_creation_screen.not_in_a_town"), false);
                return super.use(world, user, hand);
            }

            ServerPlayNetworking.send((ServerPlayerEntity)user, Packets.OPEN_NATION_CREATION_SCREEN, PacketByteBufs.create());
        }
        return super.use(world, user, hand);
    }
}
