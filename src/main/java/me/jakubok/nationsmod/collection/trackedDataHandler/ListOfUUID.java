package me.jakubok.nationsmod.collection.trackedDataHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class ListOfUUID implements TrackedDataHandler<List<UUID>> {

    @Override
    public void write(PacketByteBuf var1, List<UUID> var2) {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("size", var2.size());
        for (int i = 0; i < var2.size(); i++)
            nbt.putUuid("element" + i, var2.get(i));
        var1.writeNbt(nbt);
    }

    @Override
    public List<UUID> read(PacketByteBuf var1) {
        NbtCompound nbt = var1.readNbt();
        List<UUID> list = new ArrayList<>();
        for (int i = 0; i < nbt.getInt("size"); i++) {
            list.add(nbt.getUuid("element" + i));
        }
        return list;
    }

    @Override
    public List<UUID> copy(List<UUID> var1) {
        return var1;
    }
    
}
