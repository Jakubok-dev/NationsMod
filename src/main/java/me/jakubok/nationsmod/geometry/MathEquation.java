package me.jakubok.nationsmod.geometry;

import net.minecraft.util.math.ChunkPos;

import java.util.List;
import java.util.function.Function;

public abstract class MathEquation<T> implements Function<Double, T> {
    public final Range domain, valueSet;
    MathEquation(Range domain, Range valueSet) {
        this.domain = domain;
        this.valueSet = valueSet;
    }

    public abstract boolean isParallel(MathEquation<?> eq);
    public abstract Double getTheIntersectionX(MathEquation<?> eq);
    public abstract boolean doesOverlap(MathEquation<?> eq);
    public abstract List<ChunkPos> getOccupiedChunks();

    public static MathEquation<?> fromTwoPoints(Range domain, Range valueSet, double x1, double y1, double x2, double y2) {
        if (x1 == x2)
            return new LinearEquation(x1, valueSet);
        double a = (y1 - y2) / (x1 - x2);
        double b = y1 - a * x1;
        return new LinearFunction(domain, a, b);
    }
    public static MathEquation<?> fromTwoPoints(double x1, double y1, double x2, double y2) {
        return fromTwoPoints(new Range(x1, x2), new Range(y1, y2), x1, y1, x2, y2);
    }
}
