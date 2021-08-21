package me.jakubok.nationsmod.registries;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.jakubok.nationsmod.administration.Town;


public class TownsRegistry {
    private static List<Town> TOWNS = new ArrayList<>();

    public static List<Town> getTowns() {
        return TOWNS;
    }

    public static Town getTown(UUID id) {
        for (Town town : TOWNS) {
            if (town.getId().toString() == id.toString())
                return town;
        }
        return null;
    }

    public static boolean registerTown(Town town) {
        for (Town it : TOWNS) {
            if (it.getId().toString() == town.getId().toString())
                return false;
        }
        TOWNS.add(town);
        return true;
    }

    public static Town removeTown(UUID id) {
        for (Town town : TOWNS) {
            if (town.getId().toString() == id.toString()) {
                TOWNS.remove(town);
                return town;
            }
        }
        return null;
    }
}
