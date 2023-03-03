package me.jakubok.nationsmod.entity.human;

import java.util.PriorityQueue;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;

public class HumanInventory extends SimpleInventory implements ComponentV3 {
    PriorityQueue<Entry> swords = new PriorityQueue<>();
    PriorityQueue<Entry> axes = new PriorityQueue<>();
    PriorityQueue<Entry> shovels = new PriorityQueue<>();
    PriorityQueue<Entry> hoes = new PriorityQueue<>();

    public HumanInventory(int size) {
        super(size);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        this.refreshTheQueues();
    }

    @Override
    public ItemStack addStack(ItemStack stack) {
        ItemStack result = super.addStack(stack);
        if (result != ItemStack.EMPTY)
            refreshTheQueues();
        return result;
    }

    private void refreshTheQueues() {
        swords.clear(); axes.clear(); shovels.clear(); hoes.clear();
        for (int i = 0; i < this.size(); i++)
            putAnItemOnTheQueue(i);
    }

    public boolean putAnItemOnTheQueue(int index) {
        ItemStack stack = this.getStack(index);
        if (stack.getItem() instanceof SwordItem) {
            this.swords.add(new Entry(-((SwordItem)stack.getItem()).getMaxDamage(), index));
            return true;
        }
        if (stack.getItem() instanceof AxeItem) {
            this.axes.add(new Entry(((AxeItem)stack.getItem()).getRarity(stack).ordinal(), index));
            return true;
        }
        if (stack.getItem() instanceof ShovelItem) {
            this.shovels.add(new Entry(((ShovelItem)stack.getItem()).getRarity(stack).ordinal(), index));
            return true;
        }
        if (stack.getItem() instanceof HoeItem) {
            this.hoes.add(new Entry(((HoeItem)stack.getItem()).getRarity(stack).ordinal(), index));
            return true;
        }
        return false;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        for (int i = 0; i < tag.getInt("size"); ++i) {
            ItemStack itemStack = ItemStack.fromNbt(tag.getCompound("item" + i));
            if (itemStack.isEmpty()) continue;
            this.addStack(itemStack);
        }
        this.refreshTheQueues();
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        this.writeToNbtAndReturn(tag);
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound nbt) {
        for (int i = 0; i < this.size(); ++i) {
            ItemStack itemStack = this.getStack(i);
            if (itemStack.isEmpty()) continue;
            nbt.put("item" + i, itemStack.writeNbt(new NbtCompound()));
        }
        nbt.putInt("size", this.size());
        return nbt;
    }

    public class Entry implements Comparable<Entry> {
        private Integer key;
        private Integer value;
    
        public Entry(Integer key, Integer value) {
            this.key = key;
            this.value = value;
        }
    
        public Integer getKey() {
            return key;
        }
        public Integer getValue() {
            return value;
        }

        public void setKey(Integer key) {
            this.key = key;
        }
        public void setValue(Integer value) {
            this.value = value;
        }
    
        @Override
        public int compareTo(Entry other) {
            return this.getKey().compareTo(other.getKey());
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Entry))
                return false;
            return this.getValue().equals(((Entry)obj).getValue());
        }
    }
}
