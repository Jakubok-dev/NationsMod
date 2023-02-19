package me.jakubok.nationsmod.administration;

import java.util.UUID;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.nbt.NbtCompound;

public abstract class AdministratingUnit<D extends AdministratingUnitLawDescription> implements ComponentV3 {
    public final D description;
    public Law<D> law;
    public AdministratingUnit(D description, String name) {
        this.description = description;
        this.law = new Law<>(this.description);
        this.law.putARule(AdministratingUnitLawDescription.IdLabel, UUID.randomUUID());
        this.setName(name);
    }
    public AdministratingUnit(D description) {
        this.description = description;
        this.law = new Law<>(this.description);
    }

    public UUID getId() {
        return (UUID)this.law.getARule(AdministratingUnitLawDescription.IdLabel);
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
        tag.put("law", this.law.writeToNbtAndReturn(new NbtCompound()));
        return tag;
    }


}
