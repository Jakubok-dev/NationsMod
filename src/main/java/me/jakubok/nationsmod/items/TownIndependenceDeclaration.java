package me.jakubok.nationsmod.items;

import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class TownIndependenceDeclaration extends Item implements Declaration {

    public TownIndependenceDeclaration() {
        super(
            new FabricItemSettings()
            .rarity(Rarity.RARE)
        );
    }

    @Override
    public String type() {
        return "town_independence";
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        
        if (!world.isClient) {
            PacketByteBuf buf = PacketByteBufs.create();
            ServerPlayNetworking.send((ServerPlayerEntity)user, Packets.OPEN_TOWN_CREATION_SCREEN_PACKET, buf);
        }
        
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
