package me.jakubok.nationsmod.entity.human;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.collections.Serialisable;
import me.jakubok.nationsmod.collections.Name;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;

public class HumanData implements Serialisable {

    public Name name = new Name();
    public HumanInventory inventory = new HumanInventory(27);
    public int aggressiveness = -2;
    public List<UUID> relatives = new ArrayList<>();
    protected UUID citizenship;

    public HumanData() {}
    public HumanData(NbtCompound nbt) {
        this.readFromNbt(nbt);
    }

    public UUID getCitizenship() {
        return citizenship;
    }
    public boolean setCitizenship(UUID citizenship, MinecraftServer server) {
        if (Town.fromUUID(citizenship, server) == null)
            return false;
        this.citizenship = citizenship;
        return true;
    }
    public void removeCitizenship() {
        this.citizenship = null;
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        try {
            this.name = new Name(nbt.getCompound("name"));
        } catch(Exception ex) {
            if (this.name == null)
                this.name = new Name();
        }

        try {
            this.inventory = new HumanInventory(27);
            this.inventory.readFromNbt(nbt.getCompound("inventory"));
        } catch(Exception ex) {
            if (this.inventory == null)
                this.inventory = new HumanInventory(27);
        }

        try {
            this.aggressiveness = nbt.getInt("aggressiveness");
        } catch(Exception ex) {}

        try {
            NbtCompound relatives = nbt.getCompound("relatives");
            this.relatives.clear();
            for (int i = 0; i < relatives.getInt("size"); i++) {
                this.relatives.add(relatives.getUuid("element" + i));
            }
        } catch(Exception ex) {}

        try {
            this.citizenship = nbt.getUuid("citizenship");
        } catch(Exception ex) {}
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        this.writeToNbtAndReturn(nbt);
    }
    
    public NbtCompound writeToNbtAndReturn(NbtCompound nbt) {
        nbt.put("name", this.name.writeToNbtAndReturn(new NbtCompound()));
        nbt.put("inventory", this.inventory.writeToNbtAndReturn(new NbtCompound()));
        nbt.putInt("aggressiveness", aggressiveness);

        NbtCompound relatives = new NbtCompound();
        relatives.putInt("size", this.relatives.size());
        for (int i = 0; i < this.relatives.size(); i++)
            relatives.putUuid("element" + i, this.relatives.get(i));
        nbt.put("relatives", relatives);
        if (this.citizenship != null)
            nbt.putUuid("citizenship", this.citizenship);
        return nbt;
    }

    public static final TrackedDataHandler<HumanData> HUMAN_DATA_HANDLER = new TrackedDataHandler<HumanData>() {

        @Override
        public void write(PacketByteBuf var1, HumanData var2) {
            var1.writeNbt(var2.writeToNbtAndReturn(new NbtCompound()));
        }

        @Override
        public HumanData read(PacketByteBuf var1) {
            return new HumanData(var1.readNbt());
        }

        @Override
        public HumanData copy(HumanData var1) {
            return var1;
        }
    };
}
