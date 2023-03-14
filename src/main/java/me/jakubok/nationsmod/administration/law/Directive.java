package me.jakubok.nationsmod.administration.law;

import net.minecraft.nbt.NbtCompound;

public class Directive<D extends LawDescription> extends Law<D> {

    public Directive(D description) {
        super(description);
        this.law.clear();
    }
    public Directive(D description, NbtCompound nbt) {
        super(description);
        this.law.clear();
        super.readFromNbt(nbt);
    }
}
