package me.jakubok.nationsmod.geometry;

import net.minecraft.nbt.NbtCompound;

public class Range {
    public final double from, to;
    public final boolean isIndefinite;
    public Range(double from, double to) {
        if (from > to) {
            double temp = from;
            from = to;
            to = temp;
        }
        this.from = from;
        this.to = to;
        this.isIndefinite = false;
    }
    public Range() {
        this.isIndefinite = true;
        this.from = 0d;
        this.to = 0d;
    }

    public boolean withinRange(Double x) {
        if (x == null)
            return false;
        return x > from && x < to;
    }

    public Range commonPart(Range r) {
        if (Math.max(this.from, r.from) >= Math.min(this.to, r.to))
            return null;
        return new Range(Math.max(this.from, r.from), Math.min(this.to, r.to));
    }

    public NbtCompound writeToNbt(NbtCompound nbt) {
        nbt.putDouble("from", this.from);
        nbt.putDouble("to", this.to);
        nbt.putBoolean("isIndefinite", this.isIndefinite);
        return nbt;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Range))
            return false;
        Range r = (Range) obj;
        if (this.isIndefinite && r.isIndefinite)
            return true;
        return this.from == r.from && this.to == r.to;
    }

    public static Range readFromNbt(NbtCompound nbt) {
        double from = nbt.getDouble("from");
        double to = nbt.getDouble("to");
        boolean isIndefinite = nbt.getBoolean("isIndefinite");
        return isIndefinite ? new Range() : new Range(from, to);
    }
}