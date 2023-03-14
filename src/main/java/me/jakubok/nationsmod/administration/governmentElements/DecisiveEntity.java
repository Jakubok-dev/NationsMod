package me.jakubok.nationsmod.administration.governmentElements;

import java.util.function.Consumer;

import me.jakubok.nationsmod.administration.abstractEntities.AdministratingUnit;
import me.jakubok.nationsmod.administration.law.Directive;
import me.jakubok.nationsmod.collections.Pair;

public abstract class DecisiveEntity {

    public final AdministratingUnit<?> administratedUnit;

    public DecisiveEntity(AdministratingUnit<?> administratedUnit) {
        this.administratedUnit = administratedUnit;
    }

    public abstract void putUnderDeliberation(Directive<?> directive, Consumer<Pair<DecisiveEntitysVerdict, Directive<?>>> listener);

    public enum DecisiveEntitysVerdict {
        APPROVED,
        REJECTED
    }
}
