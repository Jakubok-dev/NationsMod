package me.jakubok.nationsmod.items;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.collections.BorderSlots;
import me.jakubok.nationsmod.collections.PlayerAccount;
import me.jakubok.nationsmod.collections.PlayerInfo;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class TownIndependenceDeclaration extends Item implements Declaration {

    public TownIndependenceDeclaration() {
        super(
            new FabricItemSettings()
            .rarity(Rarity.RARE)
            .group(NationsMod.ITEM_GROUP)
            .maxCount(1)
        );
    }

    @Override
    public String type() {
        return "town_independence";
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        
        if (!world.isClient) {
            MinecraftServer server = ((ServerWorld)world).getServer();
            BorderSlots slots = PlayerInfo.fromAccount(new PlayerAccount(user), server).slots;
            if (slots.selectedSlot == -1) {
                user.sendMessage(new TranslatableText("gui.nationsmod.border_registrator.select_a_slot"), false);
                return super.use(world, user, hand);
            }

            if (slots.getSelectedSlot().getField() == null) {
                user.sendMessage(new TranslatableText("gui.nationsmod.invalid_border"), false);
                return super.use(world, user, hand);
            }
            
            ServerPlayNetworking.send((ServerPlayerEntity)user, Packets.OPEN_TOWN_CREATION_SCREEN, PacketByteBufs.create());
        }
        
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
