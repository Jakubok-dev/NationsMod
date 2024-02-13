package me.jakubok.nationsmod.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class BorderGroup implements Serialisable {

    public BorderGroup(NbtCompound tag) {
        this.readFromNbt(tag);
    }
    public BorderGroup(String name) {
        this.name = name;
    }
    public BorderGroup() {}
    public BorderGroup(List<Border> list) {
        for (Border elem : list) 
            this.insert(elem);
    }


    public boolean validate() {
        List<Border> border = this.toList();

        if (border.size() == 0) return false;

        Boolean[] visited = new Boolean[border.size()];
        Arrays.fill(visited, false);

        Queue<Border> q = new LinkedList<Border>();
        q.add(border.get(0));

        while(!q.isEmpty()) {
            Border block = q.poll();

            if (
                visited[border.indexOf(this.get(
                block.position.getX(),
                block.position.getZ()))]
            )
                continue;

            if (this.contains(block.position.getX()-1, block.position.getZ())) {
                int nextBlockIndex = border.indexOf(
                    this.get(block.position.getX()-1, block.position.getZ())
                );

                q.add(border.get(nextBlockIndex));
            }

            if (this.contains(block.position.getX()+1, block.position.getZ())) {
                int nextBlockIndex = border.indexOf(
                    this.get(block.position.getX()+1, block.position.getZ())
                );

                q.add(border.get(nextBlockIndex));
            }

            if (this.contains(block.position.getX(), block.position.getZ()-1)) {
                int nextBlockIndex = border.indexOf(
                    this.get(block.position.getX(), block.position.getZ()-1)
                );

                q.add(border.get(nextBlockIndex));
            }

            if (this.contains(block.position.getX(), block.position.getZ()+1)) {
                int nextBlockIndex = border.indexOf(
                    this.get(block.position.getX(), block.position.getZ()+1)
                );

                q.add(border.get(nextBlockIndex));
            }

            visited[border.indexOf(this.get(
                block.position.getX(),
                 block.position.getZ()))] = true;
        }

        for (boolean element : visited)
            if (!element) return false;

        return true;
    }

    private class Sides {
        public List<BorderGroup> sides = new ArrayList<>();

        public void assimilate(BlockPos pos) {
            boolean found = false;
            for (BorderGroup element : sides) {
                if (
                    element.contains(pos.getX() - 1, pos.getZ()) ||
                    element.contains(pos.getX() + 1, pos.getZ()) ||
                    element.contains(pos.getX(), pos.getZ() - 1) ||
                    element.contains(pos.getX(), pos.getZ() + 1)
                ) {
                    if (!element.contains(pos)) {
                        element.insert(new Border(pos));
                    }

                    found = true;
                    break;
                }
            }

            if (!found) {
                BorderGroup group = new BorderGroup();
                group.insert(new Border(pos));
                this.sides.add(group);
            }
        }
    }

    public BorderGroup getField() {

        if (!this.validate()) return null;

        BorderGroup neighbouringBlocks = new BorderGroup();

        // Find neighbouring blocks
        for (Border block : this.toList()) {

            for (int x = block.position.getX() - 1; x < block.position.getX() + 2; x++) {
                for (int z = block.position.getZ() - 1; z < block.position.getZ() + 2; z++) {
                    if (this.contains(x, z) || neighbouringBlocks.contains(x, z)) continue;

                    neighbouringBlocks.insert(new Border(x, z));
                }
            }
        }

        Sides sides = new Sides();

        while (neighbouringBlocks.root != null) {
            Queue<Border> queue = new LinkedList<>();
            queue.add(neighbouringBlocks.root.value);

            while (!queue.isEmpty()) {
                Border topElement = queue.poll();
                sides.assimilate(topElement.position);
                neighbouringBlocks.delete(topElement.position.getX(), topElement.position.getZ());

                if (neighbouringBlocks.contains(
                    topElement.position.getX(), 
                    topElement.position.getZ() + 1)
                ) 
                    queue.add(neighbouringBlocks.get(
                        topElement.position.getX(), 
                        topElement.position.getZ() + 1
                    ));
                
                if (neighbouringBlocks.contains(
                    topElement.position.getX(), 
                    topElement.position.getZ() - 1)
                ) 
                    queue.add(neighbouringBlocks.get(
                        topElement.position.getX(), 
                        topElement.position.getZ() - 1
                    ));
            
                if (neighbouringBlocks.contains(
                    topElement.position.getX() + 1, 
                    topElement.position.getZ())
                ) 
                    queue.add(neighbouringBlocks.get(
                        topElement.position.getX() + 1, 
                        topElement.position.getZ()
                    ));

                if (neighbouringBlocks.contains(
                    topElement.position.getX() - 1, 
                    topElement.position.getZ())
                ) 
                    queue.add(neighbouringBlocks.get(
                        topElement.position.getX() - 1, 
                        topElement.position.getZ()
                    ));
            }
        }

        if (sides.sides.size() < 2 || sides.sides.size() > 2)
            return null;
        
        BorderGroup innerSide;

        if (sides.sides.get(0).toList().size() < sides.sides.get(1).toList().size())
            innerSide = sides.sides.get(0);
        else
            innerSide = sides.sides.get(1);

        BorderGroup field = new BorderGroup(this.toList());
        Queue<Border> queue = new LinkedList<>();

        queue.add(innerSide.root.value);

        while (!queue.isEmpty()) {
            Border topElement = queue.poll();

            if (field.contains(topElement.position))
                continue;

            field.insert(topElement);
 
            queue.add(new Border(
                topElement.position.getX(), 
                topElement.position.getZ() + 1
            ));
            queue.add(new Border(
                topElement.position.getX(), 
                topElement.position.getZ() - 1
            ));
            queue.add(new Border(
                topElement.position.getX() + 1, 
                topElement.position.getZ()
            ));
            queue.add(new Border(
                topElement.position.getX() - 1, 
                topElement.position.getZ()
            ));
        }

        return field;
    }

    public Node<Border> root;
    public String name;

    public int getBorderSize() {
        return this.toList().size();
    }

    private Node<Border> insertRecursive(Node<Border> current, Border value) {
        if (current == null) {
            current = new Node<>(value);
            return current;
        }

        if (value.position.getX() < current.value.position.getX()) {
            current.left = this.insertRecursive(current.left, value);
        }
        else if (current.value.position.getX() == value.position.getX()) {
            if (value.position.getZ() < current.value.position.getZ()) {
                current.left = this.insertRecursive(current.left, value);
            }
            else if (value.position.getZ() > current.value.position.getZ()) {
                current.right = this.insertRecursive(current.right, value);
            }
            return current;
        }
        else if (value.position.getX() > current.value.position.getX())
            current.right = this.insertRecursive(current.right, value);

        return current;
    }

    private Node<Border> deleteRecursive(Node<Border> current, int x, int z) {
        
        if (current == null)
            return null;

        if (current.value.position.getX() > x) {
            current.left = deleteRecursive(current.left, x, z);
        } else if (current.value.position.getX() < x) {
            current.right = deleteRecursive(current.right, x, z);
        } else if (current.value.position.getX() == x) {

            if (current.value.position.getZ() > z) {
                current.left = deleteRecursive(current.left, x, z);
            } else if (current.value.position.getZ() < z) {
                current.right = deleteRecursive(current.right, x, z);
            } else if (current.value.position.getZ() == z) {

                if (current.left == null)
                    return current.right;
                else if (current.right == null)
                    return current.left;

                current.value = this.minValue(current.right);

                current.right = this.deleteRecursive(current.right, current.value.position.getX(), current.value.position.getZ());
            }
        }

        return current;
    }

    public Border minValue(Node<Border> current) {
        Border minimumValue = current.value;
        while(current.left != null) {
            minimumValue = current.left.value;
            current = current.left;
        }
        return minimumValue;
    }

    public void delete(int x, int z) {
        this.root = this.deleteRecursive(root, x, z);
    }

    public void delete(Border value) {
        this.delete(value.position.getX(), value.position.getZ());
    }

    public void insert(Border value) {
        root = insertRecursive(root, value);
    }

    private Node<Border> getRecursive(Node<Border> current, int x, int z) {
        if (current == null) {
            return null;
        }

        if (x < current.value.position.getX()) {
            return this.getRecursive(current.left, x, z);
        }
        else if (current.value.position.getX() == x) {
            if (z < current.value.position.getZ()) {
                return this.getRecursive(current.left, x, z);
            }
            if (z > current.value.position.getZ()) {
                return this.getRecursive(current.right, x, z);
            }
            return current;
        }
        return this.getRecursive(current.right, x, z);
    }

    public Border get(int x, int z) {
        Node<Border> result = this.getRecursive(root, x, z);
        if (result == null) return null;
        return result.value;
    }
    public Border get(BlockPos pos) {
        return this.get(pos.getX(), pos.getZ());
    }

    public boolean contains(int x, int z) {
        return this.get(x, z) != null;
    }
    public boolean contains(BlockPos pos) {
        return this.get(pos) != null;
    }

    public List<Border> toList() {
        List<Border> arr = new ArrayList<>();
        if (root == null) return arr;
        Queue<Node<Border>> q = new LinkedList<>();
        q.add(root);

        while (!q.isEmpty()) {
            Node<Border> temp = q.remove();
            arr.add(temp.value);
            if (temp.left != null) q.add(temp.left);
            if (temp.right != null) q.add(temp.right);
        }

        return arr;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (!tag.getBoolean("is_root_null"))
            this.root = new Node<Border>(tag);
        this.name = tag.getString("name");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        if (this.root != null)
            this.root.writeToNbt(tag);
        tag.putBoolean("is_root_null", this.root == null);
        tag.putString("name", this.name);
    }
}
