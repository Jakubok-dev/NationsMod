package me.jakubok.nationsmod.administration.abstractEntities;

import java.util.Random;
import java.util.Set;
import java.util.UUID;

import me.jakubok.nationsmod.administration.governmentElements.FormOfGovernment;
import me.jakubok.nationsmod.administration.governmentElements.formsOfGovernment.AbsoluteMonarchy;
import me.jakubok.nationsmod.administration.law.Directive;
import me.jakubok.nationsmod.administration.law.LawApprovement;
import me.jakubok.nationsmod.collection.Colour;
import me.jakubok.nationsmod.collection.PlayerAccount;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;

public abstract class AdministratingUnit<D extends AdministratingUnitLawDescription> extends LegalOrganisation<D> {

    public FormOfGovernment<?, ?, ?, D> formOfGovernment;

    public AdministratingUnit(D description, String name, MinecraftServer server) {
        super(description, name, server);
        Random rng = new Random();
        if (this.getTheMapColour().getR() <= 0)
            this.getTheMapColour().setR(rng.nextInt(255));
        if (this.getTheMapColour().getG() <= 0)
            this.getTheMapColour().setG(rng.nextInt(255));
        if (this.getTheMapColour().getB() <= 0)
            this.getTheMapColour().setB(rng.nextInt(255));
        if (formOfGovernment == null)
            this.formOfGovernment = new AbsoluteMonarchy<AdministratingUnit<D>, D>(this, () -> new Directive<>(this.description), server);
    }
    public AdministratingUnit(D description) {
        super(description);
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

    public abstract Set<PlayerAccount> getPlayerMembers(MinecraftServer server);
    public abstract Set<UUID> getAIMembers(MinecraftServer server);

    public abstract void readTheFormOfGovernment(NbtCompound nbt, MinecraftServer server);

    @Override
    public void readFromNbt(NbtCompound tag, MinecraftServer server) {
        super.readFromNbt(tag, server);
        this.readTheFormOfGovernment(tag, server);
    }

    @Override
    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        tag.put("formOfGovernmentData", this.formOfGovernment.writeToNbtAndReturn(new NbtCompound()));
        tag.putString("formOfGovernment", this.formOfGovernment.getName());
        return super.writeToNbtAndReturn(tag);
    }
}
