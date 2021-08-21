package me.jakubok.nationsmod.registries;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import me.jakubok.nationsmod.administration.Town;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.WorldProperties;


public class TownsRegistry implements ComponentV3 {
    private List<Town> towns = new ArrayList<>();
    public final WorldProperties props;

    public TownsRegistry(WorldProperties props) {
        this.props = props;
    }

    public List<Town> getTowns() {
        return towns;
    }

    public Town getTown(UUID id) {
        for (Town town : towns) {
            if (town.getId().toString().equals(id.toString()))
                return town;
        }
        return null;
    }

    public boolean registerTown(Town town) {
        for (Town it : towns) {
            if (it.getId().toString().equals(town.getId().toString()))
                return false;
        }
        towns.add(town);
        return true;
    }

    public Town removeTown(UUID id) {
        for (Town town : towns) {
            if (town.getId().toString().equals(id.toString())) {
                towns.remove(town);
                return town;
            }
        }
        return null;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        for (int i = 1; i <= tag.getInt("size"); i++)
            towns.add(new Town((NbtCompound)tag.get("town" + i), props));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        AtomicInteger size = new AtomicInteger(0);
        towns.forEach(el -> {
            NbtCompound townCompound = new NbtCompound();
            el.writeToNbt(townCompound);
            tag.put("town" + size.incrementAndGet(), townCompound);
        });
        tag.putInt("size", size.get());
    }
}
