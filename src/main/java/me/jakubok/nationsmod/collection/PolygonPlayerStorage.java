package me.jakubok.nationsmod.collection;

import me.jakubok.nationsmod.geometry.Polygon;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PolygonPlayerStorage implements Serialisable {
    public List<Polygon> slots = new ArrayList<>();
    public int selectedSlot = -1;

    public PolygonPlayerStorage() {}
    public PolygonPlayerStorage(NbtCompound nbt) {
        this.readFromNbt(nbt);
    }

    public Polygon getSelectedPolygon() {
        if (this.selectedSlot == -1)
                return null;
        return this.slots.get(this.selectedSlot);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.slots.clear();
        for (int i = 1; i <= tag.getInt("size"); i++)
            slots.add(new Polygon(tag.getCompound("polygon" + i)));
        if (tag.getBoolean("selectedSlotWritten"))
            this.selectedSlot = tag.getInt("selectedSlot");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        this.writeToNbtAndReturn(tag, false);
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound nbt, boolean writeSelectedSlot) {
        if (writeSelectedSlot)
            nbt.putInt("selectedSlot", this.selectedSlot);
        nbt.putBoolean("selectedSlotWritten", writeSelectedSlot);
        AtomicInteger size = new AtomicInteger(0);
        this.slots.forEach(el -> {
            NbtCompound subtag = new NbtCompound();
            el.writeToNbt(subtag);
            nbt.put("polygon" + size.incrementAndGet(), subtag);
        });
        nbt.putInt("size", size.get());
        return nbt;
    }
}
