package me.jakubok.nationsmod.administration.governmentElements;

import java.util.UUID;
import java.util.function.Consumer;

import me.jakubok.nationsmod.administration.abstractEntities.AdministratingUnit;
import me.jakubok.nationsmod.collections.Pair;

public abstract class DecisiveEntity {

    public final AdministratingUnit<?> administratedUnit;
    public final FormOfGovernment<?, ?, ?> formOfGovernment;

    public DecisiveEntity(AdministratingUnit<?> administratedUnit, FormOfGovernment<?, ?, ?> formOfGovernment) {
        this.administratedUnit = administratedUnit;
        this.formOfGovernment = formOfGovernment;
    }

    public abstract void putUnderDeliberation(UUID directivesID, Consumer<Pair<DecisiveEntitysVerdict, UUID>> listener);

    public enum DecisiveEntitysVerdict {
        APPROVED,
        REJECTED
    }
}
