package me.jakubok.nationsmod.geometry;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.ChunkPos;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<ChunkPos> getOccupiedChunks() {
        double y1 = this.valueSet.from, y2 = this.valueSet.to;
        List<ChunkPos> lst = new ArrayList<>();
        int chunkSectionX = ((int)this.x) >> 4;
        int chunkSectionY1 = ((int)y1) >> 4;
        int chunkSectionY2 = ((int)y2) >> 4;

        if (this.valueSet.isLeftClosed || y1 > (chunkSectionY1 << 4)) {
            lst.add(new ChunkPos(chunkSectionX, chunkSectionY1));
        }

        for (int i = chunkSectionY1 + 1; i < chunkSectionY2; i++) {
            lst.add(new ChunkPos(chunkSectionX, i));
        }

        if (this.valueSet.isRightClosed || y2 > (chunkSectionY2 << 4)) {
            lst.add(new ChunkPos(chunkSectionX, chunkSectionY2));
        }

        return lst;
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
