package me.jakubok.nationsmod.administration.law;

import java.util.UUID;

import net.minecraft.nbt.NbtCompound;

public class Directive<D extends LawDescription> extends Law<D> {
    private UUID id = UUID.randomUUID();
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

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.id = tag.getUuid("directivesID");
        super.readFromNbt(tag);
    }

    @Override
    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        tag.putUuid("directivesID", this.id);
        return super.writeToNbtAndReturn(tag);
    }
}
