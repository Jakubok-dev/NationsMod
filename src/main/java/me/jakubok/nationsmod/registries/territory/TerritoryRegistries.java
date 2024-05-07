package me.jakubok.nationsmod.registries.territory;

import me.jakubok.nationsmod.NationsMod;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

public class TerritoryRegistries extends PersistentState {

    public final ServerWorld world;
    private final TerritoryRegistry districts;
    private final TerritoryRegistry provinces;

    private TerritoryRegistries(ServerWorld world) {
        this.world = world;
        this.districts = new TerritoryRegistry(world);
        this.provinces = new TerritoryRegistry(world);
    }
    private TerritoryRegistries(ServerWorld world, NbtCompound nbt) {
        this.world = world;
        this.districts = new TerritoryRegistry(world, nbt.getCompound("districts"));
        this.provinces = new TerritoryRegistry(world, nbt.getCompound("provinces"));
    }

    public TerritoryRegistry getDistricts() {
        this.markDirty();
        return districts;
    }

    public TerritoryRegistry getProvinces() {
        this.markDirty();
        return provinces;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.put("districts", this.districts.writeNbt(new NbtCompound()));
        nbt.put("provinces", this.provinces.writeNbt(new NbtCompound()));
        return nbt;
    }

    public static TerritoryRegistries getRegistry(ServerWorld world) {
        PersistentStateManager manager = world.getPersistentStateManager();
        return manager.getOrCreate(
                nbt -> new TerritoryRegistries(world, nbt),
                () -> new TerritoryRegistries(world),
                NationsMod.MOD_ID + "-territory_registries"
        );
    }
}
