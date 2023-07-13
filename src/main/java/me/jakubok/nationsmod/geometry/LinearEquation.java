package me.jakubok.nationsmod.geometry;

import net.minecraft.nbt.NbtCompound;

public class LinearEquation extends MathEquation<Boolean> {
    public final double x;
    public LinearEquation(double x, Range valueSet) {
        super(new Range(x, x), valueSet);
        this.x = x;
    }

    @Override
    public boolean isParallel(MathEquation<?> eq) {
        return eq instanceof LinearEquation;
    }

    @Override
    public Double getTheIntersectionX(MathEquation<?> eq) {
        if (eq instanceof LinearFunction function)
            return function.getTheIntersectionX(this);
        return null;
    }

    @Override
    public Boolean apply(Double value) {
        return this.valueSet.withinRange(value);
    }

    public boolean doesOverlap(MathEquation<?> eq) {
        if (!(eq instanceof LinearEquation equation))
            return false;
        return equation.x == this.x && this.valueSet.commonPart(equation.valueSet) != null;
    }

    public NbtCompound writeToNbt(NbtCompound nbt) {
        nbt.putDouble("x", this.x);
        nbt.put("valueSet", this.valueSet.writeToNbt(new NbtCompound()));
        nbt.putString("type", "linearEquation");
        return nbt;
    }

    public static LinearEquation readFromNbt(NbtCompound nbt) {
        double x = nbt.getDouble("x");
        Range valueSet = Range.readFromNbt(nbt.getCompound("valueSet"));
        return new LinearEquation(x, valueSet);
    }
}
