package me.jakubok.nationsmod.registries;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.jakubok.nationsmod.administration.TerritoryClaimer;

public class TerritoryClaimersRegistry {
    private static List<TerritoryClaimer> CLAIMERS = new ArrayList<>(); 

    public static List<TerritoryClaimer> getClaimers() {
        return CLAIMERS;
    }

    public static TerritoryClaimer getClaimer(UUID id) {
        for (TerritoryClaimer claimer : CLAIMERS) {
            if (claimer.getId().toString() == id.toString())
                return claimer;
        }
        return null;
    }

    public static boolean registerClaimer(TerritoryClaimer claimer) {
        for (TerritoryClaimer it : CLAIMERS) {
            if (it.getId().toString() == claimer.getId().toString())
                return false;
        }
        CLAIMERS.add(claimer);
        return true;
    }

    public static TerritoryClaimer removeClaimer(UUID id) {
        for (TerritoryClaimer claimer : CLAIMERS) {
            if (claimer.getId().toString() == id.toString()) {
                CLAIMERS.remove(claimer);
                return claimer;
            }
        }
        return null;
    }
}
