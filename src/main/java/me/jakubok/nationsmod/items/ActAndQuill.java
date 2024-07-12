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

public class ActAndQuill extends ActItem {
    public ActAndQuill() {
        super(
            new FabricItemSettings()
                    .maxCount(1)
        );
    }

    @Override
    public boolean isSealed() {
        return false;
    }
}
