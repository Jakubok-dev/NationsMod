package me.jakubok.nationsmod.administration.law;

import me.jakubok.nationsmod.collection.Colour;
import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.collection.Serialisable;
import net.minecraft.nbt.NbtCompound;

import java.util.*;

public abstract class LawHolder<D extends LawDescription> implements Serialisable {
    public final D description;
    protected Map<String, Object> law = new HashMap<>();
    protected LawHolder(D description) {
        this.description = description;
    }
    protected LawHolder(D description, NbtCompound nbt) {
        this.description = description;
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

    public Set<String> existingRules() {
        return this.law.keySet();
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        for (Map.Entry<String, LawDescription.RuleDescription> entry : this.description.getRulesDescriptions().entrySet()) {
            if (!tag.contains(entry.getKey()))
                continue;
            Object val;
            switch (entry.getValue().type) {
                case INTEGER -> {
                    try {
                        val = tag.getInt(entry.getKey());
                        this.putARule(entry.getKey(), val);
                    } catch (Exception ignored) {}
                }
                case STRING -> {
                    try {
                        val = tag.getString(entry.getKey());
                        this.putARule(entry.getKey(), val);
                    } catch (Exception ignored) {}
                }
                case DOUBLE -> {
                    try {
                        val = tag.getDouble(entry.getKey());
                        this.putARule(entry.getKey(), val);
                    } catch (Exception ignored) {}
                }
                case LONG -> {
                    try {
                        val = tag.getLong(entry.getKey());
                        this.putARule(entry.getKey(), val);
                    } catch (Exception ignored) {}
                }
                case UUID -> {
                    try {
                        val = tag.getUuid(entry.getKey());
                        this.putARule(entry.getKey(), val);
                    } catch (Exception ignored) {}
                }
                case COLOUR -> {
                    try {
                        val = tag.getInt(entry.getKey());
                        this.putARule(entry.getKey(), new Colour((Integer) val));
                    } catch (Exception ignored) {}
                }
                case LISTOFUUID -> {
                    try {
                        int size = tag.getInt(entry.getKey() + "Size");
                        List<UUID> list = new ArrayList<>();
                        for (int i = 0; i < size; i++)
                            list.add(tag.getUuid(entry.getKey() + i));
                        this.putARule(entry.getKey(), list);
                    } catch (Exception ignored) {}
                }
                case SETOFUUID -> {
                    try {
                        int size = tag.getInt(entry.getKey() + "Size");
                        Set<UUID> set = new HashSet<>();
                        for (int i = 0; i < size; i++)
                            set.add(tag.getUuid(entry.getKey() + i));
                        this.putARule(entry.getKey(), set);
                    } catch (Exception ignored) {}
                }
                case SETOFPLAYERACOUNT -> {
                    try {
                        int size = tag.getInt(entry.getKey() + "Size");
                        Set<PlayerAccount> set = new HashSet<>();
                        for (int i = 0; i < size; i++)
                            set.add(new PlayerAccount(tag.getCompound(entry.getKey() + i)));
                        this.putARule(entry.getKey(), set);
                    } catch (Exception ignored) {}
                }
                case LAWAPPROVEMENT -> {
                    try {
                        this.putARule(entry.getKey(), LawApprovement.values()[tag.getInt(entry.getKey())]);
                    } catch (Exception ignored) {}
                }
                case MAPOFUUIDS -> {
                    try {
                        int size = tag.getInt(entry.getKey() + "Size");
                        Map<UUID, UUID> map = new HashMap<>();
                        for (int i = 0; i < size; i++)
                            map.put(tag.getUuid(entry.getKey() + "Key" + i), tag.getUuid(entry.getKey() + "Value" + i));
                        this.putARule(entry.getKey(), map);
                    } catch (Exception ignored) {}
                }
                default -> System.out.print(entry + "of an unserialisable type");
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
            switch (this.description.getRulesDescriptions().get(entry.getKey()).type) {
                case INTEGER -> tag.putInt(entry.getKey(), (Integer) entry.getValue());
                case STRING -> tag.putString(entry.getKey(), (String) entry.getValue());
                case DOUBLE -> tag.putDouble(entry.getKey(), (Double) entry.getValue());
                case LONG -> tag.putLong(entry.getKey(), (Long) entry.getValue());
                case UUID -> tag.putUuid(entry.getKey(), (UUID) entry.getValue());
                case COLOUR -> tag.putInt(entry.getKey(), ((Colour) entry.getValue()).getBitmask());
                case LISTOFUUID -> {
                    @SuppressWarnings("unchecked")
                    List<UUID> listOfUUID = (List<UUID>) entry.getValue();
                    tag.putString(entry.getKey(), "present");
                    tag.putInt(entry.getKey() + "Size", listOfUUID.size());
                    for (int i = 0; i < listOfUUID.size(); i++)
                        tag.putUuid(entry.getKey() + i, listOfUUID.get(i));
                }
                case SETOFUUID -> {
                    @SuppressWarnings("unchecked")
                    UUID[] arrayOfUUID = ((Set<UUID>) entry.getValue()).toArray(new UUID[]{});
                    tag.putString(entry.getKey(), "present");
                    tag.putInt(entry.getKey() + "Size", arrayOfUUID.length);
                    for (int i = 0; i < arrayOfUUID.length; i++)
                        tag.putUuid(entry.getKey() + i, arrayOfUUID[i]);
                }
                case SETOFPLAYERACOUNT -> {
                    @SuppressWarnings("unchecked")
                    PlayerAccount[] arrayOfPlayerAccount = ((Set<PlayerAccount>) entry.getValue()).toArray(new PlayerAccount[]{});
                    tag.putString(entry.getKey(), "present");
                    tag.putInt(entry.getKey() + "Size", arrayOfPlayerAccount.length);
                    for (int i = 0; i < arrayOfPlayerAccount.length; i++)
                        tag.put(entry.getKey() + i, arrayOfPlayerAccount[i].writeToNbtAndReturn(new NbtCompound()));
                }
                case LAWAPPROVEMENT -> tag.putInt(entry.getKey(), ((LawApprovement) entry.getValue()).value);
                case MAPOFUUIDS -> {
                    @SuppressWarnings("unchecked")
                    Map<UUID, UUID> map = (Map<UUID, UUID>) entry.getValue();
                    List<UUID> keyList = map.keySet().stream().toList();
                    tag.putString(entry.getKey(), "present");
                    tag.putInt(entry.getKey() + "Size", keyList.size());
                    for (int i = 0; i < keyList.size(); i++) {
                        tag.putUuid(entry.getKey() + "Key" + i, keyList.get(i));
                        tag.putUuid(entry.getKey() + "Value" + i, map.get(keyList.get(i)));
                    }
                }
                default -> System.out.print(entry + "of an unserialisable type");
            }
        }
        return tag;
    }

    public String toString(String ruleName) {
        switch (this.description.getRulesDescriptions().get(ruleName).type) {
            case INTEGER -> {
                Integer integer = (Integer) this.getARule(ruleName);
                return String.valueOf(integer);
            }
            case STRING -> {
                return (String) this.getARule(ruleName);
            }
            case DOUBLE -> {
                Double _double = (Double) this.getARule(ruleName);
                return String.valueOf(_double);
            }
            case LONG -> {
                Long _long = (Long) this.getARule(ruleName);
                return String.valueOf(_long);
            }
            case UUID -> {
                UUID id = (UUID) this.getARule(ruleName);
                return id.toString();
            }
            case COLOUR -> {
                Colour colour = (Colour) this.getARule(ruleName);
                return "RED: " + colour.getR() + "; GREEN: " + colour.getG() + "; BLUE: " + colour.getB();
            }
            case LISTOFUUID, SETOFUUID, SETOFPLAYERACOUNT -> {
                Collection<?> collection = (Collection<?>) this.getARule(ruleName);
                return String.valueOf(collection.size());
            }
            case MAPOFUUIDS -> {
                Set<?> set = (Set<?>) this.getARule(ruleName);
                return String.valueOf(set.size());
            }
            case LAWAPPROVEMENT -> {
                LawApprovement approvement = (LawApprovement) this.getARule(ruleName);
                return approvement.displayText.getString();
            }
            default -> {
                return null;
            }
        }
    }
}
