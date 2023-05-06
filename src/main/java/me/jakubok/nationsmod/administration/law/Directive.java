package me.jakubok.nationsmod.administration.law;

import java.util.Map;
import java.util.UUID;

import net.minecraft.nbt.NbtCompound;

public class Directive<D extends LawDescription> extends Law<D> {
    private UUID id = UUID.randomUUID();
    public DirectiveStatus status = DirectiveStatus.UNSUBMITTED;
    public Directive(D description) {
        super(description);
        this.law.clear();
    }
    public Directive(D description, NbtCompound nbt) {
        super(description);
        this.law.clear();
        this.readFromNbt(nbt);
    }

    public UUID getID() {
        return id;
    }

    public void implement(Law<D> law) {
        for (Map.Entry<String, Object> entry : this.law.entrySet())
            law.putARule(entry.getKey(), entry.getValue());
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.id = tag.getUuid("directivesID");
        this.status = DirectiveStatus.values()[tag.getInt("status")];
        super.readFromNbt(tag);
    }

    @Override
    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        tag.putUuid("directivesID", this.id);
        tag.putInt("status", this.status.value);
        return super.writeToNbtAndReturn(tag);
    }

    public enum DirectiveStatus {
        UNSUBMITTED(0),
        DELIBERATED_BY_THE_LEGISLATIVE(1),
        REJECTED_BY_THE_LEGISLATIVE(2),
        DELIBERATED_BY_THE_EXECUTIVE(3),
        REJECTED_BY_THE_EXECUTIVE(4),
        APPROVED(5);

        DirectiveStatus(int value) {
            this.value = value;
        }

        public final int value;
    }
}
