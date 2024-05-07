package me.jakubok.nationsmod.administration.district;

import java.util.Objects;
import java.util.UUID;

import me.jakubok.nationsmod.administration.abstractEntities.TerritoryClaimer;
import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.collection.Border;
import me.jakubok.nationsmod.collection.BorderGroup;
import me.jakubok.nationsmod.collection.Colour;
import me.jakubok.nationsmod.collection.TerritoryShape;
import me.jakubok.nationsmod.geometry.Polygon;
import me.jakubok.nationsmod.networking.Packets;
import me.jakubok.nationsmod.registries.LegalOrganisationRegistry;
import me.jakubok.nationsmod.registries.territory.GameTerritoryManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class District extends TerritoryClaimer<DistrictLawDescription> {

    public District(String name, Town town, MinecraftServer server) {
        super(new DistrictLawDescription(), name, server);
        this.setTownsID(town.getId());
    }
    public District(NbtCompound tag, MinecraftServer server) {
        super(new DistrictLawDescription(), tag, server);
    }

    public Town getTown(MinecraftServer server) {
        return (Town)LegalOrganisationRegistry.getRegistry(server).get(this.getTownsID());
    }

    public UUID getTownsID() {
        return (UUID)this.law.getARule(DistrictLawDescription.townIDLabel);
    }
    public boolean setTownsID(UUID id) {
        return this.law.putARule(DistrictLawDescription.townIDLabel, id);
    }

    @Override
    public boolean claimTerritory(Polygon polygon, ServerWorld world) {
        TerritoryShape shape = GameTerritoryManager.register(polygon, this, world);
        if (shape == null)
            return false;
        return this.setTheTerritoryShape(shape);
    }

    public static District fromUUID(UUID id, MinecraftServer server) {
        return (District)LegalOrganisationRegistry.getRegistry(server).get(id);
    }
}
