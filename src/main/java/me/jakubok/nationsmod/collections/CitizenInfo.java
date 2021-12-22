package me.jakubok.nationsmod.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import me.jakubok.nationsmod.administration.Nation;
import me.jakubok.nationsmod.administration.Town;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.WorldProperties;

public class CitizenInfo implements ComponentV3 {
    protected final List<UUID> citizenships = new ArrayList<>();
    protected UUID nationality = null;
    public final WorldProperties props;

    public CitizenInfo(WorldProperties props) {
        this.props = props;
    }
    public CitizenInfo(NbtCompound nbt, WorldProperties props) {
        this.readFromNbt(nbt);
        this.props = props;
    }

    public boolean addCitizenship(Town town) {
        
        for (UUID id : this.citizenships) {
            if (id.equals(town.getId()))
                return false;
        }

        if (this.nationality == null && town.hasProvince() == true && citizenships.size() > 0)
            return false;
        else if (this.nationality == null && town.hasProvince() == true && citizenships.size() == 0) {
            this.nationality = town.getProvince().getNation().getId();
            this.citizenships.add(town.getId());
            return true;
        }
        else if (this.nationality == null && town.hasProvince() == false) {
            this.citizenships.add(town.getId());
            return true;
        }
        else if (!this.nationality.equals(town.getProvince().getNation().getId()))
            return false;
        
        this.citizenships.add(town.getId());
        return true;
    }

    public boolean removeCitizenship(Town town) {
        
        int index = -1;
        boolean found = false;
        while (index < this.citizenships.size()) {
            index++;
            if (this.citizenships.get(index).equals(town.getId())) {
                found = true;
                break;
            }
        }

        if (index == -1 || !found)
            return false;

        if (this.citizenships.size() < 2)
            this.nationality = null;

        this.citizenships.remove(this.citizenships.get(index));
        return true;
    }

    public boolean hasCitizenship(Town town) {
        int index = -1;
        boolean found = false;
        while (index < this.citizenships.size()) {
            index++;
            if (this.citizenships.get(index).equals(town.getId())) {
                found = true;
                break;
            }
        }

        if (index == -1 || !found)
            return false;
        return true;
    }

    public boolean setNationality(Nation nation) {
        for (Town town : this.citizenships
            .stream()
            .map(el -> Town.fromUUID(el, this.props))
            .toList()
        ) {
            if (!town.hasProvince())
                return false;
            else if (town.getProvince().getNation() != nation)
                return false;
        }

        this.nationality = nation.getId();
        return true;
    }

    public Nation getNationality() {
        if (this.nationality == null)
            return null;

        return Nation.fromUUID(this.nationality, this.props);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (!tag.getBoolean("is_nationality_null"))
            this.nationality = tag.getUuid("nationality");

        this.citizenships.clear();
        for (int i = 1; i <= tag.getInt("citizenships_size"); i++)
            this.citizenships.add(tag.getUuid("citizenship" + i));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        this.writeToNbtAndReturn(tag);
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        AtomicInteger size = new AtomicInteger(0);

        this.citizenships.forEach(el -> {
            tag.putUuid("citizenship" + size.incrementAndGet(), el);
        });
        tag.putInt("citizenships_size", size.get());

        if (this.nationality != null)
            tag.putUuid("nationality", this.nationality);
        tag.putBoolean("is_nationality_null", this.nationality == null);

        return tag;
    }
}
