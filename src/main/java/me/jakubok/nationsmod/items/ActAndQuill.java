package me.jakubok.nationsmod.items;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.administration.abstractEntities.LegalOrganisation;
import me.jakubok.nationsmod.administration.abstractEntities.LegalOrganisationLawDescription;
import me.jakubok.nationsmod.administration.law.Act;
import me.jakubok.nationsmod.administration.nation.Nation;
import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.administration.town.TownLawDescription;
import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.collection.PlayerInfo;
import me.jakubok.nationsmod.networking.Packets;
import me.jakubok.nationsmod.registries.LegalOrganisationRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.UUID;

public class ActAndQuill extends Item {
    public ActAndQuill() {
        super(
            new FabricItemSettings()
                    .maxCount(1)
        );
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        NbtCompound nbt = stack.getSubNbt(NationsMod.MOD_ID);
        if (nbt == null)
            return super.hasGlint(stack);
        UUID bodyID = nbt.getUuid("bodyID");
        NbtCompound actNbt = nbt.getCompound("act");
        return bodyID != null && actNbt != null || super.hasGlint(stack);
    }

    public Act<?> getTheAct(ItemStack stack, MinecraftServer server) {
        NbtCompound nbt = stack.getSubNbt(NationsMod.MOD_ID);
        if (nbt == null)
            return null;
        UUID bodyID = nbt.getUuid("bodyID");
        NbtCompound actNbt = nbt.getCompound("act");
        if (bodyID == null || actNbt == null)
            return null;
        LegalOrganisation<?> organisation = LegalOrganisationRegistry.getRegistry(server).get(bodyID);
        if (organisation == null)
            return null;
        return new Act<>(organisation.description, actNbt);
    }

    @SuppressWarnings("unchecked")
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient)
            return super.use(world, user, hand);

        ItemStack stack = user.getMainHandStack();
        stack.getSubNbt(NationsMod.MOD_ID + "_act_id");
        NbtCompound nbt = stack.getSubNbt(NationsMod.MOD_ID);
        if (nbt == null) {
            PlayerInfo info = PlayerInfo.fromAccount(new PlayerAccount(user), user.getServer());
            if (info.getCitizenship() == null) {
                user.sendMessage(Text.of("To create an act, you must be a citizen of any town"));
                return TypedActionResult.fail(user.getMainHandStack());
            }
            Town town = Town.fromUUID(info.getCitizenship(), user.getServer());
            if (town == null) {
                user.sendMessage(Text.of("To create an act, you must be a citizen of any town"));
                return TypedActionResult.fail(user.getMainHandStack());
            }

            NbtCompound sentNbt = new NbtCompound();
            sentNbt.putUuid("townID", town.getId());
            sentNbt.putString("townName", town.getName());
            Nation nation = null;
            if (town.getNationsID() != null) {
                nation = town.getNation(user.getServer());
                if (nation != null) {
                    sentNbt.putUuid("nationID", nation.getId());
                    sentNbt.putString("nationName", nation.getName());
                }
            }
            sentNbt.putBoolean("is_nation_null", nation == null);
            PacketByteBuf buffer = PacketByteBufs.create();
            buffer.writeNbt(sentNbt);
            ServerPlayNetworking.send((ServerPlayerEntity) user, Packets.OPEN_ACT_CREATION_SCREEN, buffer);
        }

        Act<?> rawAct = this.getTheAct(stack, user.getServer());
        if (rawAct.description instanceof TownLawDescription) {
            Act<TownLawDescription> act = (Act<TownLawDescription>)rawAct;
            Town town = (Town)act.getAffectedBody(user.getServer());
            NbtCompound sentNbt = new NbtCompound();
            sentNbt.put("act", act.writeToNbtAndReturn(new NbtCompound()));
            sentNbt.put("town", town.writeToNbtAndReturn(new NbtCompound()));
            PacketByteBuf buffer = PacketByteBufs.create();
            buffer.writeNbt(sentNbt);
            ServerPlayNetworking.send((ServerPlayerEntity) user, Packets.OPEN_TOWN_SCREEN_WITH_A_PETITION, buffer);
        }

        return TypedActionResult.success(user.getMainHandStack());
    }
}
