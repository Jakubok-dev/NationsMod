package me.Jakubok.nations.collections;

public class Leaf<T> {
    public T value;
    protected Leaf<T> left;
    protected Leaf<T> right;

    Leaf(T value) {
        this.value = value;
        left = null;
        right = null;
    }
}
