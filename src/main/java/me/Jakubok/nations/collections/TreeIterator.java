package me.Jakubok.nations.collections;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TreeIterator<T> implements Iterator {

    public Node<T> value;
    TreeIterator<T> previous;
    Map<String, Boolean> visited = new HashMap<>();

    public TreeIterator(Node<T> value, TreeIterator<T> previous) {
        this.value = value;
        this.previous = previous;
        visited.put("left", false);
        visited.put("right", false);
    }

    protected int howManyChildren() {
        if (value.left == null && value.right == null) return 0;
        else if (value.left == null || value.right == null) return 1;
        return 2;
    }

    @Nullable
    protected Node<T> getChild() {
        if (howManyChildren() == 1) {
            if (value.left == null && !visited.get("right"))
                return value.right;
            else if (value.right == null && !visited.get("left")) return value.left;
            return null;
        }
        else if (howManyChildren() == 0) return null;

        if (visited.get("left") && !visited.get("right")) return value.right;
        else if (visited.get("left") && visited.get("right")) return null;
        return value.left;
    }

    @Override
    public boolean hasNext() {
        return getChild() != null;
    }

    @Override
    public TreeIterator next() {
        Node<T> temp = getChild();
        if (temp == null) {
            if (previous == null) return null;
            return previous.next();
        }
        if (temp == value.right)
            visited.put("right", true);
        else
            visited.put("left", true);
        return new TreeIterator<T>(temp, this);
    }
}
