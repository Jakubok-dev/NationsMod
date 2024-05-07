package me.jakubok.nationsmod.administration.province;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.jakubok.nationsmod.administration.abstractEntities.TerritoryClaimer;
import me.jakubok.nationsmod.administration.nation.Nation;
import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.collection.Colour;
import me.jakubok.nationsmod.collection.TerritoryShape;
import me.jakubok.nationsmod.geometry.Polygon;
import me.jakubok.nationsmod.registries.LegalOrganisationRegistry;
import me.jakubok.nationsmod.registries.territory.GameTerritoryManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class Province extends TerritoryClaimer<ProvinceLawDescription> {

    public Province(String name, Town capital, Nation nation, MinecraftServer server) {
        super(new ProvinceLawDescription(), name, server);
        this.setNationsUUID(nation.getId());
        this.setCapitalsUUID(capital.getId(), server);
        capital.setProvince(this, server);
    }
    public Province(NbtCompound tag, MinecraftServer server) {
        super(new ProvinceLawDescription(), tag, server);
    }

    public Town getCapital(MinecraftServer server) {
        return Town.fromUUID(getCapitalsUUID(server), server);
    }
    public UUID getCapitalsUUID(MinecraftServer server) {
        Nation nation = this.getNation(server);
        if (nation == null)
            return null;
        return nation.getProvincesCapitalRegistry().get(this.getId());
    }
    public boolean setCapitalsUUID(UUID id, MinecraftServer server) {
        Nation nation = this.getNation(server);
        if (nation == null)
            return false;
        return nation.getProvincesCapitalRegistry().put(this.getId(), id) != null;
    }
    
    public List<Town> getTowns(MinecraftServer server) {
        return this.getTownsIDs(server).stream()
        .map(el -> Town.fromUUID(el, server))
        .toList();
    }
    public List<UUID> getTownsIDs(MinecraftServer server) {
        Nation nation = this.getNation(server);
        if (nation == null)
            return null;
        return nation.getTownProvinceRegistry().entrySet().stream().filter(e -> e.getValue().equals(this.getId())).map(Map.Entry::getKey).toList();
    }

    public Nation getNation(MinecraftServer server) {
        return Nation.fromUUID(this.getNationsUUID(), server);
    }
    public UUID getNationsUUID() {
        return (UUID)this.law.getARule(ProvinceLawDescription.nationsIDLabel);
    }
    public boolean setNationsUUID(UUID id) {
        return this.law.putARule(ProvinceLawDescription.nationsIDLabel, id);
    }

    @Override
    public boolean claimTerritory(Polygon polygon, ServerWorld world) {
        TerritoryShape shape = GameTerritoryManager.register(polygon, this, world);
        if (shape == null)
            return false;
        return this.setTheTerritoryShape(shape);
    }

    public static Province fromUUID(UUID id, MinecraftServer server) {
        return (Province)LegalOrganisationRegistry.getRegistry(server).get(id);
    }
}
