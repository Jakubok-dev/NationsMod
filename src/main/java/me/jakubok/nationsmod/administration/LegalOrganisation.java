package me.jakubok.nationsmod.administration;

import java.util.UUID;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import me.jakubok.nationsmod.registries.ComponentsRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.WorldProperties;

public abstract class LegalOrganisation<D extends LegalOrganisationLawDescription> implements ComponentV3 {
    public final D description;
    public Law<D> law;
    public final WorldProperties props;
    public LegalOrganisation(D description, String name, WorldProperties props) {
        this.description = description;
        this.law = new Law<>(this.description);
        this.law.putARule(LegalOrganisationLawDescription.IdLabel, UUID.randomUUID());
        this.setName(name);
        this.props = props;
        ComponentsRegistry.LEGAL_ORGANISATIONS_REGISTRY.get(this.props).register(this);
    }
    public LegalOrganisation(D description, WorldProperties props) {
        this.description = description;
        this.props = props;
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

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.law.readFromNbt(tag.getCompound("law"));
    }

    @Override
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
