package me.jakubok.nationsmod.geometry;

import me.jakubok.nationsmod.collection.Pair;
import me.jakubok.nationsmod.collection.Serialisable;
import net.minecraft.nbt.NbtCompound;

public class Point extends Pair<Integer, Integer> implements Serialisable {

    public Point(int x, int z) {
        super(x, z);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.key = tag.getInt("x");
        this.value = tag.getInt("z");
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        tag.putInt("x", this.key);
        tag.putInt("z", this.value);
        return tag;
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        this.writeToNbtAndReturn(tag);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point))
            return false;
        Point p = (Point) obj;
        return p.key.equals(this.key) && p.value.equals(this.value);
    }
}
