package me.jakubok.nationsmod.registries;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import me.jakubok.nationsmod.administration.Nation;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.WorldProperties;

public class NationsRegistry implements ComponentV3 {
    private List<Nation> nations = new ArrayList<>(); 
    public final WorldProperties props;

    public NationsRegistry(WorldProperties props) {
        this.props = props;
    }

    public List<Nation> getNations() {
        return nations;
    }

    public Nation getNation(UUID id) {
        if (id == null)
            return null;
        for (Nation nation : nations) {
            if (nation.getId().toString().equals(id.toString()))
                return nation;
        }
        return null;
    }

    public boolean registerNation(Nation nation) {
        for (Nation it : nations) {
            if (it.getId().toString().equals(nation.getId().toString()))
                return false;
        }
        nations.add(nation);
        return true;
    }

    public Nation removeNation(UUID id) {
        for (Nation nation : nations) {
            if (nation.getId().toString().equals(id.toString())) {
                nations.remove(nation);
                return nation;
            }
        }
        return null;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        for (int i = 1; i <= tag.getInt("size"); i++) {
            NbtCompound compound = (NbtCompound)tag.get("nation" + i);
            nations.add(new Nation(compound, props));
        } 
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        AtomicInteger size = new AtomicInteger(0);
        nations.forEach(el -> {
            NbtCompound compound = new NbtCompound();
            el.writeToNbt(compound);
            tag.put("nation" + size.incrementAndGet(), compound);
        });
        tag.putInt("size", size.get());
    }
}
