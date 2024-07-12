package me.jakubok.nationsmod.administration.abstractEntities;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import me.jakubok.nationsmod.administration.governmentElements.FormOfGovernment;
import me.jakubok.nationsmod.administration.governmentElements.formsOfGovernment.AbsoluteMonarchy;
import me.jakubok.nationsmod.administration.law.LawApprovement;
import me.jakubok.nationsmod.administration.law.Petition;
import me.jakubok.nationsmod.collection.Colour;
import me.jakubok.nationsmod.collection.PlayerAccount;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;

public abstract class AdministratingUnit<D extends AdministratingUnitLawDescription> extends LegalOrganisation<D> {

    public FormOfGovernment<?, ?, ?, D> formOfGovernment;
    public Map<UUID, Petition<D>> petitions;

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
            this.formOfGovernment = new AbsoluteMonarchy<>(this, server);
        this.petitions = new HashMap<>();
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
    public boolean isACitizen(MinecraftServer server, PlayerAccount account) {
        return this.getPlayerMembers(server).contains(account);
    }
    public boolean isACitizen(MinecraftServer server, UUID npc) {
        return this.getNPCMembers(server).contains(npc);
    }
    public abstract Set<UUID> getNPCMembers(MinecraftServer server);

    public abstract void readTheFormOfGovernment(NbtCompound nbt, MinecraftServer server);

    public boolean signAPetition(MinecraftServer server, PlayerAccount account, UUID petitionID) {
        if (!this.isACitizen(server, account))
            return false;
        Petition<D> petition = this.petitions.get(petitionID);
        if (petition == null)
            return false;
        boolean result = petition.playerSignees.add(account);
        this.onPetitionSign(server, petition);
        return result;
    }
    public boolean signAPetition(MinecraftServer server, UUID npc, UUID petitionID) {
        if (!this.isACitizen(server, npc))
            return false;
        Petition<D> petition = this.petitions.get(petitionID);
        if (petition == null)
            return false;
        boolean result = petition.npcSignees.add(npc);
        this.onPetitionSign(server, petition);
        return result;
    }
    protected void onPetitionSign(MinecraftServer server, Petition<D> petition) {
        int signeesCount = petition.npcSignees.size() + petition.playerSignees.size();
        int citizensCount = this.getPlayerMembers(server).size() + this.getNPCMembers(server).size();
        if ((signeesCount / citizensCount) >= (this.getThePetitionSupport() / 100)) {
            this.petitions.remove(petition.act.getID());
            this.formOfGovernment.putUnderDeliberation(petition.act);
        }
    }

    public boolean unsignFromAPetition(MinecraftServer server, PlayerAccount account, UUID petitionID) {
        if (!this.isACitizen(server, account))
            return false;
        Petition<D> petition = this.petitions.get(petitionID);
        if (petition == null)
            return false;
        return petition.playerSignees.remove(account);
    }
    public boolean unsignFromAPetition(MinecraftServer server, UUID npc, UUID petitionID) {
        if (!this.isACitizen(server, npc))
            return false;
        Petition<D> petition = this.petitions.get(petitionID);
        if (petition == null)
            return false;
        return petition.npcSignees.remove(npc);
    }

    @Override
    public void readFromNbt(NbtCompound tag, MinecraftServer server) {
        super.readFromNbt(tag, server);
        this.readTheFormOfGovernment(tag, server);
        this.petitions = new HashMap<>();
        for (int i = 0; i < tag.getInt("petitionsSize"); i++) {
            Petition<D> petition = new Petition<>(this.description, tag.getCompound("petition" + i));
            this.petitions.put(petition.act.getID(), petition);
        }
    }

    @Override
    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        tag.put("formOfGovernmentData", this.formOfGovernment.writeToNbtAndReturn(new NbtCompound()));
        tag.putString("formOfGovernment", this.formOfGovernment.getName());
        tag.putInt("petitionsSize", this.petitions.size());
        AtomicInteger integer = new AtomicInteger(0);
        for (UUID id : this.petitions.keySet())
            tag.put("petition" + integer.getAndIncrement(), this.petitions.get(id).writeToNbtAndReturn(new NbtCompound()));
        return super.writeToNbtAndReturn(tag);
    }
}
