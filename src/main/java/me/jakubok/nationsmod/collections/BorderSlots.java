package me.jakubok.nationsmod.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.nbt.NbtCompound;

public class BorderSlots implements ComponentV3 {

    public List<BorderGroup> slots = new ArrayList<>();

    public BorderSlots() {}
    public BorderSlots(NbtCompound nbt) {
        this.readFromNbt(nbt);
    }
    
    public int selectedSlot = -1;
    public boolean isSelected(int index) {
        return index == selectedSlot;
    }

    public BorderGroup getSelectedSlot() {
        if (selectedSlot == -1) return null;
        return slots.get(selectedSlot);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.slots.clear();
        for (int i = 1; i <= tag.getInt("size"); i++)
            slots.add(new BorderGroup((NbtCompound)tag.get("slot" + i)));
        this.selectedSlot = tag.getInt("selectedSlot");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        writeToNbtAndReturn(tag);
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        AtomicInteger size = new AtomicInteger(0);
        this.slots.forEach(el -> {
            NbtCompound subtag = new NbtCompound();
            el.writeToNbt(subtag);
            tag.put("slot" + size.incrementAndGet(), subtag);
        });
        tag.putInt("size", size.get());
        tag.putInt("selectedSlot", this.selectedSlot);
        return tag;
    }
    
}
