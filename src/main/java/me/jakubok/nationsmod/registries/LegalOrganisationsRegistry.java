package me.jakubok.nationsmod.registries;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import me.jakubok.nationsmod.administration.District;
import me.jakubok.nationsmod.administration.LegalOrganisation;
import me.jakubok.nationsmod.administration.Nation;
import me.jakubok.nationsmod.administration.Province;
import me.jakubok.nationsmod.administration.Town;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.WorldProperties;

public class LegalOrganisationsRegistry implements ComponentV3 {

    private final Map<UUID, LegalOrganisation<?>> organisations = new HashMap<>();
    public final WorldProperties props;
    
    public LegalOrganisationsRegistry(WorldProperties props) {
        this.props = props;
    }

    public Map<UUID, LegalOrganisation<?>> getOrganisations() {
        return this.organisations;
    }

    public LegalOrganisation<?> get(UUID id) {
        return this.organisations.get(id);
    }

    public boolean register(LegalOrganisation<?> organisation) {
        if (organisation == null)
            return false;
        if (this.organisations.get(organisation.getId()) != null)
            return false;
        this.organisations.put(organisation.getId(), organisation);
        return true;
    }

    public boolean remove(UUID id) {
        return this.organisations.remove(id) != null;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.organisations.clear();
        for (int i = 0; i < tag.getInt("size"); i++) {
            NbtCompound subtag = tag.getCompound("entry" + i);
            UUID id = subtag.getCompound("law").getUuid("id");
            if (subtag.getBoolean("isADistrict"))
            this.organisations.put(id, new District(subtag, props));
            if (subtag.getBoolean("isATown"))
            this.organisations.put(id, new Town(subtag, props));
            if (subtag.getBoolean("isAProvince"))
                this.organisations.put(id, new Province(subtag, props));
            if (subtag.getBoolean("isANation")) 
                this.organisations.put(id, new Nation(subtag, props));
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        this.writeToNbtAndReturn(tag);
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound nbt) {
        AtomicInteger size = new AtomicInteger(0);
        for (LegalOrganisation<?> entry : this.organisations.values())
            nbt.put("entry" + size.getAndIncrement(), entry.writeToNbtAndReturn(new NbtCompound()));
        nbt.putInt("size", size.get());
        return nbt;
    }
}
