package me.jakubok.nationsmod.registries;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import me.jakubok.nationsmod.NationsMod;
import me.jakubok.nationsmod.administration.abstractEntities.LegalOrganisation;
import me.jakubok.nationsmod.administration.district.District;
import me.jakubok.nationsmod.administration.nation.Nation;
import me.jakubok.nationsmod.administration.province.Province;
import me.jakubok.nationsmod.administration.town.Town;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

public class LegalOrganisationRegistry extends PersistentState {

    private final Map<UUID, LegalOrganisation<?>> organisations = new HashMap<>();

    public Map<UUID, LegalOrganisation<?>> getOrganisations() {
        this.markDirty();
        return this.organisations;
    }

    public LegalOrganisation<?> get(UUID id) {
        this.markDirty();
        return this.organisations.get(id);
    }

    public boolean register(LegalOrganisation<?> organisation) {
        if (organisation == null)
            return false;
        if (this.organisations.get(organisation.getId()) != null)
            return false;
        this.organisations.put(organisation.getId(), organisation);
        this.markDirty();
        return true;
    }

    public boolean remove(UUID id) {
        this.markDirty();
        return this.organisations.remove(id) != null;
    }

    public void readFromNbt(NbtCompound tag, MinecraftServer server) {
        this.markDirty();
        this.organisations.clear();
        for (int i = 0; i < tag.getInt("size"); i++) {
            NbtCompound subtag = tag.getCompound("entry" + i);
            UUID id = subtag.getCompound("law").getUuid("id");
            if (subtag.getBoolean("isADistrict"))
            this.organisations.put(id, new District(subtag, server));
            if (subtag.getBoolean("isATown"))
            this.organisations.put(id, new Town(subtag, server));
            if (subtag.getBoolean("isAProvince"))
                this.organisations.put(id, new Province(subtag, server));
            if (subtag.getBoolean("isANation")) 
                this.organisations.put(id, new Nation(subtag, server));
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        AtomicInteger size = new AtomicInteger(0);
        for (LegalOrganisation<?> entry : this.organisations.values())
            nbt.put("entry" + size.getAndIncrement(), entry.writeToNbtAndReturn(new NbtCompound()));
        nbt.putInt("size", size.get());
        return nbt;
    }

    public static LegalOrganisationRegistry getRegistry(MinecraftServer server) {

        Function<NbtCompound, LegalOrganisationRegistry> createFromNbt = nbt -> {
            LegalOrganisationRegistry registry = new LegalOrganisationRegistry();
            registry.readFromNbt(nbt, server);
            return registry;
        };

        PersistentStateManager manager = server.getOverworld().getPersistentStateManager();
        LegalOrganisationRegistry registry = manager.getOrCreate(
            createFromNbt,
            LegalOrganisationRegistry::new, 
            NationsMod.MOD_ID + ":legal_organisations_registry"
        );
        return registry;
    }
}
