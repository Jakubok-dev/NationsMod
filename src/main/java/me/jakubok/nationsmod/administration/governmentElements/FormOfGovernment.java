package me.jakubok.nationsmod.administration.governmentElements;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.jakubok.nationsmod.administration.abstractEntities.AdministratingUnit;
import me.jakubok.nationsmod.administration.abstractEntities.AdministratingUnitLawDescription;
import me.jakubok.nationsmod.administration.governmentElements.DecisiveEntity.DecisiveEntitysVerdict;
import me.jakubok.nationsmod.administration.law.Act;
import me.jakubok.nationsmod.collection.Serialisable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public abstract class FormOfGovernment<L extends DecisiveEntity, E extends DecisiveEntity, U extends AdministratingUnit<D>, D extends AdministratingUnitLawDescription> implements Serialisable {
    public final U administratedUnit;
    public Map<UUID, Act<D>> mapOfDirectives;

    public FormOfGovernment(U administratedUnit) {
        this.administratedUnit = administratedUnit;
        this.mapOfDirectives = new HashMap<>();
    }
    public FormOfGovernment(U administratedUnit, NbtCompound nbt) {
        this.administratedUnit = administratedUnit;
        this.readFromNbt(nbt);
    }

    public abstract L getLegislative();
    public abstract E getExecutive();

    public abstract void putUnderDeliberation(Act<D> act);

    public abstract String getName();
    public abstract Text getDisplayName();
    public abstract Text getDescription();

    public abstract void legislativesVerdictListener(UUID directivesID, DecisiveEntitysVerdict verdict);
    public abstract void executivesVerdictListener(UUID directivesID, DecisiveEntitysVerdict verdict);

    @Override
    public void readFromNbt(NbtCompound nbt) {
        this.mapOfDirectives = new HashMap<>();
        for (int i = 0; i < nbt.getInt("Size"); i++) {
            UUID id = nbt.getUuid("actsID" + i);
            Act<D> directive = new Act<>(administratedUnit.description, nbt.getCompound("act" + i));
            this.mapOfDirectives.put(id, directive);
        }
    }
    @Override
    public void writeToNbt(NbtCompound nbt) {
        this.writeToNbtAndReturn(nbt);
    }
    public NbtCompound writeToNbtAndReturn(NbtCompound nbt) {
        UUID[] uuids = this.mapOfDirectives.keySet().toArray(new UUID[]{});
        for (int i = 0; i < uuids.length; i++) {
            nbt.putUuid("actsID" + i, uuids[i]);
            nbt.put("act" + i, this.mapOfDirectives.get(uuids[i]).writeToNbtAndReturn(new NbtCompound()));
        }
        nbt.putInt("Size", uuids.length);
        return nbt;
    }
}
