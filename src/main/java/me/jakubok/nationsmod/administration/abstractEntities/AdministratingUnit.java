package me.jakubok.nationsmod.administration.abstractEntities;

import java.util.Random;
import java.util.Set;
import java.util.UUID;

import me.jakubok.nationsmod.administration.governmentElements.FormOfGovernment;
import me.jakubok.nationsmod.administration.governmentElements.formsOfGovernment.AbsoluteMonarchy;
import me.jakubok.nationsmod.administration.law.Directive;
import me.jakubok.nationsmod.administration.law.LawApprovement;
import me.jakubok.nationsmod.collections.Colour;
import me.jakubok.nationsmod.collections.PlayerAccount;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.WorldProperties;

public abstract class AdministratingUnit<D extends AdministratingUnitLawDescription> extends LegalOrganisation<D> {

    public FormOfGovernment<?, ?, ?, D> formOfGovernment;

    public AdministratingUnit(D description, String name, WorldProperties props) {
        super(description, name, props);
        Random rng = new Random();
        if (this.getTheMapColour().getR() <= 0)
            this.getTheMapColour().setR(rng.nextInt(255));
        if (this.getTheMapColour().getG() <= 0)
            this.getTheMapColour().setG(rng.nextInt(255));
        if (this.getTheMapColour().getB() <= 0)
            this.getTheMapColour().setB(rng.nextInt(255));
        if (formOfGovernment == null)
            this.formOfGovernment = new AbsoluteMonarchy<AdministratingUnit<D>, D>(this, () -> new Directive<>(this.description));
    }
    public AdministratingUnit(D description, WorldProperties props) {
        super(description, props);
    }

    public Colour getTheMapColour() {
        return (Colour)this.law.getARule(AdministratingUnitLawDescription.mapColourLabel);
    }

    public int getThePetitionSupport() {
        return (int)this.law.getARule(AdministratingUnitLawDescription.petitionSupportLabel);
    }

    public LawApprovement getTheCitizenshipApprovement() {
        return (LawApprovement)this.law.getARule(AdministratingUnitLawDescription.citizenshipApprovementLabel);
    }

    public abstract Set<PlayerAccount> getPlayerMembers();
    public abstract Set<UUID> getAIMembers();

    public abstract void readTheFormOfGovernment(NbtCompound nbt);

    @Override
    public void readFromNbt(NbtCompound tag) {
        super.readFromNbt(tag);
        this.readTheFormOfGovernment(tag);
    }

    @Override
    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        tag.put("formOfGovernmentData", this.formOfGovernment.writeToNbtAndReturn(new NbtCompound()));
        tag.putString("formOfGovernment", this.formOfGovernment.getName());
        return super.writeToNbtAndReturn(tag);
    }
}
