package me.jakubok.nationsmod.administration;

import java.util.UUID;

import me.jakubok.nationsmod.registries.TerritoryClaimersRegistry;
import me.jakubok.nationsmod.registries.TownsRegistry;

public class District implements TerritoryClaimer {

    private String name;
    private final UUID id = UUID.randomUUID();
    private UUID townId;

    public District(String name, Town town) {
        this.name = name;
        this.townId = town.getId();
        TerritoryClaimersRegistry.registerClaimer(this);
    }

    @Override
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Town getTown() {
        return TownsRegistry.getTown(townId);
    }
}
