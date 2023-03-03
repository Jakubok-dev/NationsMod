package me.jakubok.nationsmod.entity.human;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import me.jakubok.nationsmod.collections.Name;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class HumanData implements ComponentV3 {

    public Name name = new Name();
    public HumanInventory inventory = new HumanInventory(27);

    public HumanData() {}
    public HumanData(NbtCompound nbt) {
        this.readFromNbt(nbt);
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
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        this.writeToNbtAndReturn(nbt);
    }
    
    public NbtCompound writeToNbtAndReturn(NbtCompound nbt) {
        nbt.put("name", this.name.writeToNbtAndReturn(new NbtCompound()));
        nbt.put("inventory", this.inventory.writeToNbtAndReturn(new NbtCompound()));
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
