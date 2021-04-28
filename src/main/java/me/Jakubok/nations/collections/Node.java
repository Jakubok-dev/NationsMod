package me.Jakubok.nations.collections;

public class Node<T> {
    public T value;
    public Node<T> left;
    public Node<T> right;

    public Node(T value) {
        this.value = value;
        left = null;
        right = null;
    }
}
