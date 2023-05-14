package me.jakubok.nationsmod.administration.governmentElements;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import me.jakubok.nationsmod.administration.abstractEntities.AdministratingUnit;
import me.jakubok.nationsmod.collection.Serialisable;
import net.minecraft.nbt.NbtCompound;

public abstract class DecisiveEntity implements Serialisable {

    public final AdministratingUnit<?> administratedUnit;
    public final FormOfGovernment<?, ?, ?, ?> formOfGovernment;
    public final Set<UUID> deliberatedDirectives = new HashSet<>();

    public DecisiveEntity(AdministratingUnit<?> administratedUnit, FormOfGovernment<?, ?, ?, ?> formOfGovernment) {
        this.administratedUnit = administratedUnit;
        this.formOfGovernment = formOfGovernment;
    }

    public boolean putUnderDeliberation(UUID directivesID) {
        return this.deliberatedDirectives.add(directivesID);
    }

    public enum DecisiveEntitysVerdict {
        APPROVED,
        REJECTED
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.deliberatedDirectives.clear();
        for (int i = 0; i < tag.getInt("Size"); i++)
            this.deliberatedDirectives.add(tag.getUuid("directivesID" + i));
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        AtomicInteger size = new AtomicInteger(0);
        for (UUID id : this.deliberatedDirectives)
            tag.putUuid("directivesID" + size.getAndIncrement(), id);
        tag.putInt("Size", size.get());
        return tag;
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        this.writeToNbtAndReturn(tag);
    }
}
