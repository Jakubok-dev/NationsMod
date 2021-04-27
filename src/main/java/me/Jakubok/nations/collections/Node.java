package me.Jakubok.nations.collections;

public class Node<T> {
    public T value;
    protected Node<T> left;
    protected Node<T> right;

    Node(T value) {
        this.value = value;
        left = null;
        right = null;
    }
}
