package me.jakubok.nationsmod.administration.law;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import me.jakubok.nationsmod.administration.law.LawDescription.RuleDescription;
import me.jakubok.nationsmod.collections.Colour;
import me.jakubok.nationsmod.collections.PlayerAccount;
import net.minecraft.nbt.NbtCompound;

public class Law<D extends LawDescription> implements ComponentV3 {
    public final D description;
    protected Map<String, Object> law = new HashMap<>();
    public Law(D description) {
        this.description = description;
        for (Map.Entry<String, RuleDescription> ruleDescription : this.description.getRulesDescriptions().entrySet()) {
            this.putARule(ruleDescription.getKey(), ruleDescription.getValue().defaultValue);
        }
    }
    public Law(D description, NbtCompound nbt) {
        this.description = description;
        for (Map.Entry<String, RuleDescription> ruleDescription : this.description.getRulesDescriptions().entrySet()) {
            this.putARule(ruleDescription.getKey(), ruleDescription.getValue().defaultValue);
        }
        this.readFromNbt(nbt);
    }

    public Object getARule(String ruleName) {
        return law.get(ruleName);
    }

    public boolean putARule(String ruleName, Object object) {
        if (this.description.getRulesDescriptions().get(ruleName) == null)
            return false;
        if (!this.description.getRulesDescriptions().get(ruleName).compatible(object))
            return false;
        this.law.put(ruleName, object);
        return true;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        for (Map.Entry<String, RuleDescription> entry : this.description.getRulesDescriptions().entrySet()) {
            Object val;
            switch(entry.getValue().type) {
                case INTEGER:
                    try {
                        val = tag.getInt(entry.getKey());
                        this.putARule(entry.getKey(), val);
                    } catch(Exception ex) {}
                        break;
                case STRING:
                    try {
                        val = tag.getString(entry.getKey());
                        this.putARule(entry.getKey(), val);
                    } catch(Exception ex) {}
                    break;
                case DOUBLE:
                    try {
                        val = tag.getDouble(entry.getKey());
                        this.putARule(entry.getKey(), val);
                    } catch(Exception ex) {}
                    break;
                case LONG:
                    try {
                        val = tag.getLong(entry.getKey());
                        this.putARule(entry.getKey(), val);
                    } catch(Exception ex) {}
                    break;
                case UUID:
                    try {
                        val = tag.getUuid(entry.getKey());
                        this.putARule(entry.getKey(), val);
                    } catch(Exception ex) {}
                    break;
                case COLOUR:
                    try {
                        val = tag.getInt(entry.getKey());
                        this.putARule(entry.getKey(), new Colour((Integer)val));
                    } catch(Exception ex) {}
                    break;
                case LISTOFUUID:
                    try {
                        int size = tag.getInt(entry.getKey() + "Size");
                        List<UUID> list = new ArrayList<>();
                        for (int i = 0; i < size; i++)
                            list.add(tag.getUuid(entry.getKey() + i));
                        this.putARule(entry.getKey(), list);
                    } catch(Exception ex) {}
                    break;
                case SETOFUUID:
                    try {
                        int size = tag.getInt(entry.getKey() + "Size");
                        Set<UUID> set = new HashSet<>();
                        for (int i = 0; i < size; i++)
                            set.add(tag.getUuid(entry.getKey() + i));
                        this.putARule(entry.getKey(), set);
                    } catch(Exception ex) {}
                    break;
                case SETOFPLAYERACOUNT:
                    try {
                        int size = tag.getInt(entry.getKey() + "Size");
                        Set<PlayerAccount> set = new HashSet<>();
                        for (int i = 0; i < size; i++)
                            set.add(new PlayerAccount(tag.getCompound(entry.getKey() + i)));
                        this.putARule(entry.getKey(), set);
                    } catch(Exception ex) {}
                    break;
                default:
                    System.out.print(entry + "of an unserialisable type");
                    break;
            }
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        this.writeToNbtAndReturn(tag);
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        for (Map.Entry<String, Object> entry : this.law.entrySet()) {
            if (entry.getValue() == null)
                continue;
            if (!this.description.getRulesDescriptions().get(entry.getKey()).compatible(entry.getValue())) {
                System.out.print(entry + "of an uncompatible type");
                continue;
            }
            switch(this.description.getRulesDescriptions().get(entry.getKey()).type) {
                case INTEGER:
                    tag.putInt(entry.getKey(), (Integer)entry.getValue());
                    break;
                case STRING:
                    tag.putString(entry.getKey(), (String)entry.getValue());
                    break;
                case DOUBLE:
                    tag.putDouble(entry.getKey(), (Double)entry.getValue());
                    break;
                case LONG:
                    tag.putLong(entry.getKey(), (Long)entry.getValue());
                    break;
                case UUID:
                    tag.putUuid(entry.getKey(), (UUID)entry.getValue());
                    break;
                case COLOUR:
                    tag.putInt(entry.getKey(), ((Colour)entry.getValue()).getBitmask());
                    break;
                case LISTOFUUID:
                    @SuppressWarnings("unchecked")
                    List<UUID> listOfUUID = (List<UUID>)entry.getValue();
                    tag.putInt(entry.getKey() + "Size", listOfUUID.size());
                    for (int i = 0; i < listOfUUID.size(); i++)
                        tag.putUuid(entry.getKey() + i, listOfUUID.get(i));
                    break;
                case SETOFUUID:
                    @SuppressWarnings("unchecked")
                    UUID[] arrayOfUUID = ((Set<UUID>)entry.getValue()).toArray(new UUID[]{});
                    tag.putInt(entry.getKey() + "Size", arrayOfUUID.length);
                    for (int i = 0; i < arrayOfUUID.length; i++)
                        tag.putUuid(entry.getKey() + i, arrayOfUUID[i]);
                    break;
                case SETOFPLAYERACOUNT:
                    @SuppressWarnings("unchecked")
                    PlayerAccount[] arrayOfPlayerAccount = ((Set<PlayerAccount>)entry.getValue()).toArray(new PlayerAccount[]{});
                    tag.putInt(entry.getKey() + "Size", arrayOfPlayerAccount.length);
                    for (int i = 0; i < arrayOfPlayerAccount.length; i++)
                        tag.put(entry.getKey() + i, arrayOfPlayerAccount[i].writeToNbtAndReturn(new NbtCompound()));
                    break;
                default:
                    System.out.print(entry + "of an unserialisable type");
                    break;
            }
        }
        return tag;
    }
}
