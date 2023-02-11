package me.jakubok.nationsmod.registries;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import me.jakubok.nationsmod.administration.District;
import me.jakubok.nationsmod.administration.Province;
import me.jakubok.nationsmod.administration.TerritoryClaimer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.WorldProperties;

public class TerritoryClaimersRegistry implements ComponentV3 {
    private List<TerritoryClaimer> claimers = new ArrayList<>(); 
    public final WorldProperties props;

    public TerritoryClaimersRegistry(WorldProperties props) {
        this.props = props;
    }

    public List<TerritoryClaimer> getClaimers() {
        return claimers;
    }

    public TerritoryClaimer getClaimer(UUID id) {
        if (id == null)
            return null;
        for (TerritoryClaimer claimer : claimers) {
            if (claimer.getId().toString().equals(id.toString()))
                return claimer;
        }
        return null;
    }

    public boolean registerClaimer(TerritoryClaimer claimer) {
        for (TerritoryClaimer it : claimers) {
            if (it.getId().toString().equals(claimer.getId().toString()))
                return false;
        }
        claimers.add(claimer);
        return true;
    }

    public TerritoryClaimer removeClaimer(UUID id) {
        for (TerritoryClaimer claimer : claimers) {
            if (claimer.getId().toString().equals(id.toString())) {
                claimers.remove(claimer);
                return claimer;
            }
        }
        return null;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        claimers.clear();
        for (int i = 1; i <= tag.getInt("size"); i++) {
            NbtCompound claimerCompound = (NbtCompound)tag.get("claimer" + i);
            if (claimerCompound.getBoolean("district"))
                claimers.add(new District(claimerCompound, props));
            if (claimerCompound.getBoolean("province"))
                claimers.add(new Province(claimerCompound, props));
        } 
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        writeToNbtAndReturn(tag);
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        AtomicInteger size = new AtomicInteger(0);
        claimers.forEach(el -> {
            NbtCompound claimerCompound = new NbtCompound();
            el.writeToNbt(claimerCompound);
            tag.put("claimer" + size.incrementAndGet(), claimerCompound);
        });
        tag.putInt("size", size.get());
        return tag;
    }
}
