package me.jakubok.nationsmod.map;

import java.util.UUID;

import me.jakubok.nationsmod.collections.Pair;

public class MapBlockInfo {
    public Pair<String, UUID> district;
    public Pair<String, UUID> town;

    public MapBlockInfo(Pair<String, UUID> district, Pair<String, UUID> town) {
        this.district = district;
        this.town = town;
    }
}
