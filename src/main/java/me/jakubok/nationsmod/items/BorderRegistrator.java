package me.jakubok.nationsmod.items;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.collections.BorderSlots;
import me.jakubok.nationsmod.networking.Packets;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BorderRegistrator extends Item {

    public BorderRegistrator() {
        super(new FabricItemSettings().group(NationsMod.ITEM_GROUP).maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient)
            return super.use(world, user, hand);

        PacketByteBuf buffer = PacketByteBufs.create();
        BorderSlots playersSlots = ComponentsRegistry.BORDER_SLOTS.get(user);

        NbtCompound tag = new NbtCompound();
        tag.putInt("size", playersSlots.slots.size());
        for (int i = 0; i < playersSlots.slots.size(); i++) {
            tag.putString("name" + (i + 1), playersSlots.slots.get(i).name);
            tag.putInt("index" + (i + 1), i);
        }

        buffer.writeNbt(tag);

        ServerPlayNetworking.send((ServerPlayerEntity)user, Packets.OPEN_BORDER_REGISTRATOR_SCREEN_PACKET, buffer);

        return super.use(world, user, hand);
    }
}
