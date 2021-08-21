package me.jakubok.nationsmod.administration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.jakubok.nationsmod.registries.TerritoryClaimersRegistry;
import me.jakubok.nationsmod.registries.TownsRegistry;


public class Town {
    private String name;
    private UUID id = UUID.randomUUID();
    private List<UUID> districtsIDs = new ArrayList<>();

    public Town(String name) {
        this.name = name;
        TownsRegistry.registerTown(this);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public List<District> getDistricts() {
        return districtsIDs.stream()
        .map(el -> (District)TerritoryClaimersRegistry.getClaimer(el))
        .toList();
    }
}
