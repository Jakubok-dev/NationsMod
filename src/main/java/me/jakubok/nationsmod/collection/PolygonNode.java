package me.jakubok.nationsmod.collection;

import net.minecraft.nbt.NbtCompound;

public class PolygonNode<T extends Serialisable> {
    public T value;
    public PolygonNode<T> left;
    public PolygonNode<T> right;

    public PolygonNode(T value) {
        this.value = value;
        left = null;
        right = null;
    }
}
