package me.jakubok.nationsmod.geometry;

import me.jakubok.nationsmod.collection.Pair;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class LinearFunction extends MathEquation<Double> {
    public final double a, b;
    LinearFunction(Range domain, double a, double b) {
        super(domain, new Range(a * domain.from + b, a * domain.to + b, true, true));
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
            return this.apply(equation.x) != null && equation.apply(this.apply(equation.x)) ? equation.x : null;
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

    public Double getX(Double y) {
        if (this.a == 0 || !this.valueSet.withinRange(y))
            return null;
        return this.domain.withinRange((y - this.b) / this.a) ? (y - this.b) / this.a : null;
    }

    @Override
    public boolean doesOverlap(MathEquation<?> eq) {
        if (!(eq instanceof LinearFunction fun))
            return false;
        return this.a == fun.a && this.b == fun.b && this.domain.commonPart(fun.domain) != null && this.valueSet.commonPart(fun.valueSet) != null;
    }

    @Override
    public List<ChunkPos> getOccupiedChunks() {
        double x1 = this.domain.from, y1 = a >= 0 ? this.valueSet.from : this.valueSet.to, x2 = this.domain.to, y2 = a >= 0 ? this.valueSet.to : this.valueSet.from;
        int chunkSectionX = ((int)x1) >> 4, chunkSectionY = ((int)y1) >> 4;
        PriorityQueue<Pair<Double, Double>> q = new PriorityQueue<>(Comparator.comparingDouble(p -> p.key));
        q.add(new Pair<>(x1, y1));
        q.add(new Pair<>(x2, y2));

        for (int i = (chunkSectionX << 4) + 16; i < x2; i += 16) {
            q.add(new Pair<>((double)i, this.apply((double)i)));
        }

        if (a > 0) {
            for (int i = (chunkSectionY << 4) + 16; i < y2; i += 16) {
                q.add(new Pair<>(this.getX((double)i), (double)i));
            }
        } else if (a < 0) {
            for (int i = (chunkSectionY << 4) - 16; i > y2; i -= 16) {
                q.add(new Pair<>(this.getX((double)i), (double)i));
            }
        }

        List<ChunkPos> lst = new ArrayList<>();
        if (this.domain.isLeftClosed)
            lst.add(new ChunkPos(BlockPos.ofFloored(x1, 64d, y1)));

        while (q.size() > 1) {
            Pair<Double, Double> first = q.poll(), second = q.peek();
            if (second == null)
                break;
            Pair<Double, Double> avg = new Pair<>((first.key + second.key) / 2, (first.value + second.value) / 2);
            ChunkPos pos = new ChunkPos(BlockPos.ofFloored(avg.key, 64d, avg.value));
            if (!lst.isEmpty()) {
                if (!lst.get(lst.size() - 1).equals(pos))
                    lst.add(pos);
            }
            else lst.add(pos);
        }

        if (this.domain.isRightClosed) {
            if (!lst.isEmpty()) {
                if (!lst.get(lst.size() - 1).equals(new ChunkPos(BlockPos.ofFloored(x2, 64d, y2))))
                    lst.add(new ChunkPos(BlockPos.ofFloored(x2, 64d, y2)));
            } else lst.add(new ChunkPos(BlockPos.ofFloored(x2, 64d, y2)));
        }

        return lst;
    }

    public NbtCompound writeToNbt(NbtCompound nbt) {
        nbt.put("domain", this.domain.writeToNbt(new NbtCompound()));
        nbt.putDouble("a", this.a);
        nbt.putDouble("b", this.b);
        nbt.putString("type", "linearFunction");
        return nbt;
    }

    public static LinearFunction readFromNbt(NbtCompound nbt) {
        Range domain = Range.readFromNbt(nbt.getCompound("domain"));
        double a = nbt.getDouble("a");
        double b = nbt.getDouble("b");
        return new LinearFunction(domain, a, b);
    }
}
