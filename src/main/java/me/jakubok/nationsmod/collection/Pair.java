package me.jakubok.nationsmod.collection;

import me.jakubok.nationsmod.geometry.Point;

public class Pair<A, B> {
    public A key;
    public B value;
    public Pair(A key, B value) {
        this.key = key;
        this.value = value;
    }
}
