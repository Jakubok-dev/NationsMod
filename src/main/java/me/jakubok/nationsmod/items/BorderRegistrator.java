package me.jakubok.nationsmod.items;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.collections.Border;
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
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BorderRegistrator extends Item {

    public BorderRegistrator() {
        super(new FabricItemSettings().group(NationsMod.ITEM_GROUP).maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient)
            return super.use(world, user, hand);

        if (user.isSneaking()) {
            openBorderRegistratorScreen(user);
            return super.use(world, user, hand);
        }

        BlockPos position = user.getCameraBlockPos();
        BorderSlots slots = ComponentsRegistry.BORDER_SLOTS.get(user);

        if (slots.getSelectedSlot() == null)
            user.sendMessage(new TranslatableText("gui.nationsmod.border_registrator.select_a_slot"), true);

        if (slots.getSelectedSlot().contains(position)) {
            slots.getSelectedSlot().delete(position.getX(), position.getZ());

            TranslatableText firstUnmarkMessage = new TranslatableText("gui.nationsmod.border_registrator.unmark.1");
            TranslatableText secondUnmarkMessage = new TranslatableText("gui.nationsmod.border_registrator.unmark.2");
            TranslatableText thirdUnmarkMessage = new TranslatableText("gui.nationsmod.border_registrator.unmark.3");

            Text unmarkMessage = Text.of(
                firstUnmarkMessage.getString() + " " +
                "X:" + position.getX() + "; Z:" + position.getZ() + "; " +
                secondUnmarkMessage.getString() + " " +
                slots.getSelectedSlot().name + " " +
                thirdUnmarkMessage.getString()
            );

            user.sendMessage(unmarkMessage, true);

        } else {
            slots.getSelectedSlot().insert(new Border(position));

            TranslatableText firstMarkMessage = new TranslatableText("gui.nationsmod.border_registrator.mark.1");
            TranslatableText secondMarkMessage = new TranslatableText("gui.nationsmod.border_registrator.mark.2");
            TranslatableText thirdMarkMessage = new TranslatableText("gui.nationsmod.border_registrator.mark.3");

            Text markMessage = Text.of(
                firstMarkMessage.getString() + " " +
                "X:" + position.getX() + "; Z:" + position.getZ() + "; " +
                secondMarkMessage.getString() + " " +
                slots.getSelectedSlot().name + " " +
                thirdMarkMessage.getString()
            );

            user.sendMessage(markMessage, true);
        }

        return super.use(world, user, hand);
    }

    private void openBorderRegistratorScreen(PlayerEntity user) {
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
    }
}
