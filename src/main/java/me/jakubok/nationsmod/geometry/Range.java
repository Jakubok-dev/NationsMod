package me.jakubok.nationsmod.geometry;

import net.minecraft.nbt.NbtCompound;

public class Range {
    public final double from, to;
    public final boolean isLeftClosed, isRightClosed;
    public final boolean isIndefinite;

    public Range(double from, double to, boolean isLeftClosed, boolean isRightClosed) {
        if (from > to) {
            double temp = from;
            from = to;
            to = temp;
        }
        this.from = from;
        this.to = to;
        this.isIndefinite = false;
        this.isLeftClosed = isLeftClosed;
        this.isRightClosed = isRightClosed;
    }
    public Range(double from, double to) {
        this(from, to, false, false);
    }
    public Range() {
        this.isIndefinite = true;
        this.from = 0d;
        this.to = 0d;
        this.isLeftClosed = false;
        this.isRightClosed = false;
    }

    public boolean withinRange(Double x) {
        if (x == null)
            return false;
        if (this.isIndefinite)
            return true;
        boolean a = this.isLeftClosed ? x >= from : x > from;
        boolean b = this.isRightClosed ? x <= to : x < to;
        return a && b;
    }

    public Range commonPart(Range r) {
        if (r == null)
            return null;
        if (this.isIndefinite)
            return r;
        if (r.isIndefinite)
            return this;

        Range leftRange = this.to < r.to ? this : r;
        Range rightRange = this.to > r.to ? this : r;
        double commonFrom = rightRange.from;
        double commonTo = leftRange.to;
        if (commonFrom > commonTo || commonFrom == commonTo && (!leftRange.isRightClosed || !rightRange.isLeftClosed))
            return null;
        return new Range(commonFrom, commonTo, rightRange.isLeftClosed, leftRange.isRightClosed);
    }

    public NbtCompound writeToNbt(NbtCompound nbt) {
        nbt.putDouble("from", this.from);
        nbt.putDouble("to", this.to);
        nbt.putBoolean("isIndefinite", this.isIndefinite);
        nbt.putBoolean("isLeftClosed", this.isLeftClosed);
        nbt.putBoolean("isRightClosed", this.isRightClosed);
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
        boolean isLeftClosed = nbt.getBoolean("isLeftClosed");
        boolean isRightClosed = nbt.getBoolean("isRightClosed");
        return isIndefinite ? new Range() : new Range(from, to, isLeftClosed, isRightClosed);
    }
}