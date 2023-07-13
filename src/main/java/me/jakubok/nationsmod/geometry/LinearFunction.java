package me.jakubok.nationsmod.geometry;

import net.minecraft.nbt.NbtCompound;

public class LinearFunction extends MathEquation<Double> {
    public final double a, b;
    LinearFunction(Range domain, Range valueSet, double a, double b) {
        super(domain, valueSet);
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean isParallel(MathEquation<?> eq) {
        if (!(eq instanceof LinearFunction))
            return false;
        return ((LinearFunction)eq).a == this.a;
    }

    @Override
    public Double getTheIntersectionX(MathEquation<?> eq) {
        if (eq instanceof LinearEquation equation) {
            return this.apply(equation.x) != null && equation.apply(this.apply(equation.x)) != null ? equation.x : null;
        } else if (eq instanceof LinearFunction function) {
            if (function.a == this.a)
                return null;
            double result = (function.b - this.b) / (this.a - function.a);
            return this.apply(result) != null && function.apply(result) != null ? result : null;
        }
        return null;
    }

    @Override
    public Double apply(Double x) {
        if (!this.domain.withinRange(x))
            return null;
        return this.valueSet.withinRange(a * x + b) ? a * x + b : null;
    }

    @Override
    public boolean doesOverlap(MathEquation<?> eq) {
        if (!(eq instanceof LinearFunction fun))
            return false;
        return this.a == fun.a && this.b == fun.b && this.domain.commonPart(fun.domain) != null && this.valueSet.commonPart(fun.valueSet) != null;
    }

    public NbtCompound writeToNbt(NbtCompound nbt) {
        nbt.put("domain", this.domain.writeToNbt(new NbtCompound()));
        nbt.put("valueSet", this.valueSet.writeToNbt(new NbtCompound()));
        nbt.putDouble("a", this.a);
        nbt.putDouble("b", this.b);
        nbt.putString("type", "linearFunction");
        return nbt;
    }

    public static LinearFunction readFromNbt(NbtCompound nbt) {
        Range domain = Range.readFromNbt(nbt.getCompound("domain"));
        Range valueSet = Range.readFromNbt(nbt.getCompound("valueSet"));
        double a = nbt.getDouble("a");
        double b = nbt.getDouble("b");
        return new LinearFunction(domain, valueSet, a, b);
    }
}
