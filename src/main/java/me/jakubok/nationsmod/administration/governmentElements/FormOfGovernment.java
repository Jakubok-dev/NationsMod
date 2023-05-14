package me.jakubok.nationsmod.administration.governmentElements;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import me.jakubok.nationsmod.administration.abstractEntities.AdministratingUnit;
import me.jakubok.nationsmod.administration.abstractEntities.AdministratingUnitLawDescription;
import me.jakubok.nationsmod.administration.governmentElements.DecisiveEntity.DecisiveEntitysVerdict;
import me.jakubok.nationsmod.administration.law.Directive;
import me.jakubok.nationsmod.collection.Serialisable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public abstract class FormOfGovernment<L extends DecisiveEntity, E extends DecisiveEntity, U extends AdministratingUnit<D>, D extends AdministratingUnitLawDescription> implements Serialisable {
    public final U administratedUnit;
    public final Map<UUID, Directive<D>> mapOfDirectives = new HashMap<>();
    protected final Supplier<Directive<D>> directiveFactory;

    public FormOfGovernment(U administratedUnit, Supplier<Directive<D>> directiveFactory) {
        this.administratedUnit = administratedUnit;
        this.directiveFactory = directiveFactory;
    }

    public abstract L getLegislative();
    public abstract E getExecutive();

    public abstract void putUnderDeliberation(Directive<D> directive);

    public abstract String getName();
    public abstract Text getDisplayName();
    public abstract Text getDescription();

    public abstract void legislativesVerdictListener(UUID directivesID, DecisiveEntitysVerdict verdict);
    public abstract void executivesVerdictListener(UUID directivesID, DecisiveEntitysVerdict verdict);

    @Override
    public void readFromNbt(NbtCompound nbt) {
        this.mapOfDirectives.clear();
        for (int i = 0; i < nbt.getInt("Size"); i++) {
            UUID id = nbt.getUuid("directivesID" + i);
            Directive<D> directive = this.directiveFactory.get();
            directive.readFromNbt(nbt.getCompound("directive" + i));
            this.mapOfDirectives.put(id, directive);
        }
        this.getLegislative().readFromNbt(nbt.getCompound("legislative"));
        if (this.getLegislative() != this.getExecutive())
            this.getExecutive().readFromNbt(nbt.getCompound("executive"));
    }
    @Override
    public void writeToNbt(NbtCompound nbt) {
        this.writeToNbtAndReturn(nbt);
    }
    public NbtCompound writeToNbtAndReturn(NbtCompound nbt) {
        UUID[] uuids = this.mapOfDirectives.keySet().toArray(new UUID[]{});
        for (int i = 0; i < uuids.length; i++) {
            nbt.putUuid("directivesID" + i, uuids[i]);
            nbt.put("directive" + i, this.mapOfDirectives.get(uuids[i]).writeToNbtAndReturn(new NbtCompound()));
        }
        nbt.putInt("Size", uuids.length);
        nbt.put("legislative", this.getLegislative().writeToNbtAndReturn(new NbtCompound()));
        nbt.put("executive", this.getExecutive().writeToNbtAndReturn(new NbtCompound()));
        return nbt;
    }
}
