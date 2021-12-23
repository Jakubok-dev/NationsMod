package me.jakubok.nationsmod.administration;

import java.util.ArrayList;
import java.util.List;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import me.jakubok.nationsmod.collections.PlayerAccount;
import net.minecraft.nbt.NbtCompound;

public class Directive<C> implements ComponentV3 {
    List<DirectivesEntry> directivesEntries = new ArrayList<>();
    Class<C> object;
    public String title;
    public DirectiveStatus status;
    public PlayerAccount legislator;
    public List<PlayerAccount> signatures = new ArrayList<>();

    public Directive(String title, PlayerAccount legislator, Class<C> object) {
        this.object = object;
        this.legislator = legislator;
        this.title = title;
        status = DirectiveStatus.COLLECTING_SIGNATURES;
    }

    public Directive(Class<C> object, NbtCompound tag) {
        this.object = object;
        this.readFromNbt(tag);
    }

    public void sign(PlayerAccount account, Integer citizensCount, float approvementAmongTheCitizens) {
        for (PlayerAccount elem : this.signatures)
            if (elem.equals(account))
                return;
        
        signatures.add(account);
        this.checkStatus(citizensCount, approvementAmongTheCitizens);
    }

    public void checkStatus(Integer citizensCount, float approvementAmongTheCitizens) {
        if (this.signatures.size() >= Math.round(citizensCount.floatValue() * approvementAmongTheCitizens)) {
            status = DirectiveStatus.IN_LEGISLATION;
        } else {
            status = DirectiveStatus.COLLECTING_SIGNATURES;
        }
    }

    public List<DirectivesEntry> getDirectivesEntries() {
        return directivesEntries;
    }

    public boolean addAnEntry(String key, Object value) {
        try {
            if (!object.getField(key).getType().equals(value.getClass()))
                return false;

            directivesEntries.add(new DirectivesEntry(key, value));
        } catch(Exception e) {
            return false;
        }
        return true;
    }

    public DirectivesEntry removeAnEntry(int index) {
        return this.directivesEntries.remove(index);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.title = tag.getString("title");
        this.status = DirectiveStatus.valueOf(tag.getString("status"));
        this.legislator = new PlayerAccount(tag.getCompound("legislator"));
        this.signatures.clear();
        for (int i = 1; i <= this.signatures.size(); i++)
            this.signatures.add(new PlayerAccount(tag.getCompound("signature" + i)));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        this.writeToNbtAndReturn(tag);
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        
        tag.putString("title", this.title);
        tag.putString("status", this.status.name());
        tag.put("legislator", this.legislator.writeToNbtAndReturn(new NbtCompound()));
        for (int i = 0; i < this.signatures.size(); i++) 
            tag.put("signature" + (i + 1), this.signatures.get(i).writeToNbtAndReturn(new NbtCompound()));

        return tag;
    }
}
