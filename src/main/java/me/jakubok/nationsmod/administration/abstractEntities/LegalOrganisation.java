package me.jakubok.nationsmod.administration.abstractEntities;

import java.util.UUID;

import me.jakubok.nationsmod.administration.district.District;
import me.jakubok.nationsmod.administration.law.Law;
import me.jakubok.nationsmod.administration.nation.Nation;
import me.jakubok.nationsmod.administration.province.Province;
import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.administration.town.TownLawDescription;
import me.jakubok.nationsmod.registries.LegalOrganisationsRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;

public abstract class LegalOrganisation<D extends LegalOrganisationLawDescription> {
    public final D description;
    public Law<D> law;
    public LegalOrganisation(D description, String name, MinecraftServer server) {
        this.description = description;
        this.law = new Law<>(this.description);
        this.law.putARule(LegalOrganisationLawDescription.IdLabel, UUID.randomUUID());
        this.setName(name);
        LegalOrganisationsRegistry.getRegistry(server).register(this);
    }
    public LegalOrganisation(D description) {
        this.description = description;
        this.law = new Law<>(this.description);
    }

    public UUID getId() {
        return (UUID)this.law.getARule(LegalOrganisationLawDescription.IdLabel);
    }

    public String getName() {
        return (String)this.law.getARule(TownLawDescription.NameLabel);
    }
    public boolean setName(String name) {
        return this.law.putARule(TownLawDescription.NameLabel, name);
    }

    public void readFromNbt(NbtCompound tag, MinecraftServer server) {
        this.law.readFromNbt(tag.getCompound("law"));
    }

    public void writeToNbt(NbtCompound tag) {
        this.writeToNbtAndReturn(tag);
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        tag.putBoolean("isADistrict", this instanceof District);
        tag.putBoolean("isAProvince", this instanceof Province);
        tag.putBoolean("isATown", this instanceof Town);
        tag.putBoolean("isANation", this instanceof Nation);
        tag.put("law", this.law.writeToNbtAndReturn(new NbtCompound()));
        return tag;
    }


}
