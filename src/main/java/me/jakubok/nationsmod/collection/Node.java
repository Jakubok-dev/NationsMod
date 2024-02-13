package me.jakubok.nationsmod.collection;

import com.google.common.base.Supplier;

import net.minecraft.nbt.NbtCompound;

public class Node<T extends Serialisable> implements Serialisable {
    public T value;
    public Node<T> left;
    public Node<T> right;

    public Node(T value) {
        this.value = value;
        left = null;
        right = null;
    }

    public Node(NbtCompound tag) {
        this.readFromNbt(tag);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (!tag.getBoolean("is_left_null"))
            this.left = new Node<T>((NbtCompound)tag.get("left"));

        if (!tag.getBoolean("is_right_null"))
            this.right = new Node<T>((NbtCompound)tag.get("right"));

        this.value.readFromNbt((NbtCompound)tag.get("value"));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtCompound valueCompound = new NbtCompound();
        this.value.writeToNbt(valueCompound);
        tag.put("value", valueCompound);

        if (left != null) {
            NbtCompound leftCompound = new NbtCompound();
            this.left.writeToNbt(leftCompound);
            tag.put("left", leftCompound);
        }
        tag.putBoolean("is_left_null", left == null);

        if (right != null) {
            NbtCompound rightCompound = new NbtCompound();
            this.right.writeToNbt(rightCompound);
            tag.put("right", rightCompound);
        }
        tag.putBoolean("is_right_null", right == null);
    }

    public int getChildsCount() {
        int count = 0;

        if (this.left != null)
            count++;
        if (this.right != null)
            count++;

        return count;
    }
}
