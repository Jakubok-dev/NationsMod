package me.jakubok.nationsmod.administration.law;

import me.jakubok.nationsmod.administration.abstractEntities.LegalOrganisationLawDescription;
import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.collection.Serialisable;
import net.minecraft.nbt.NbtCompound;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Petition<D extends LegalOrganisationLawDescription> implements Serialisable {
    public Act<D> act;
    public final D description;
    public Set<PlayerAccount> playerSignees;
    public Set<UUID> npcSignees;

    public Petition(Act<D> act, D description) {
        this.act = act;
        this.description = description;
        this.playerSignees = new HashSet<>();
        this.npcSignees = new HashSet<>();
    }

    public Petition(D description, NbtCompound nbt) {
        this.description = description;
        this.readFromNbt(nbt);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.act = new Act<>(description, tag.getCompound("act"));
        this.playerSignees = new HashSet<>();
        for (int i = 0; i < tag.getInt("playerSigneesSize"); i++)
            this.playerSignees.add(new PlayerAccount(tag.getCompound("playerSignee" + i)));
        this.npcSignees = new HashSet<>();
        for (int i = 0; i < tag.getInt("npcSigneesSize"); i++)
            this.npcSignees.add(tag.getUuid("npcSignee" + i));
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        tag.put("act", this.act.writeToNbtAndReturn(new NbtCompound()));

        PlayerAccount[] arrayOfPlayerAccount = this.playerSignees.toArray(new PlayerAccount[]{});
        tag.putInt("playerSigneesSize", arrayOfPlayerAccount.length);
        for (int i = 0; i < arrayOfPlayerAccount.length; i++)
            tag.put("playerSignee" + i, arrayOfPlayerAccount[i].writeToNbtAndReturn(new NbtCompound()));

        UUID[] arrayOfUUID = this.npcSignees.toArray(new UUID[]{});
        tag.putInt("npcSigneesSize", arrayOfUUID.length);
        for (int i = 0; i < arrayOfUUID.length; i++)
            tag.putUuid("npcSignee" + i, arrayOfUUID[i]);

        return tag;
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        this.writeToNbtAndReturn(tag);
    }
}
