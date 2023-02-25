package me.jakubok.nationsmod.collections;

import java.util.HashMap;
import java.util.Map;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;

public abstract class Registry<K, V extends ComponentV3> implements ComponentV3 {
    protected Map<K, V> registry = new HashMap<>();

    public V get(K key) {
        return registry.get(key);
    }

    public boolean register(K key, V value) {
        return registry.put(key, value) != null;
    }

    public boolean unregister(K key) {
        return registry.remove(key) != null;
    }
}
