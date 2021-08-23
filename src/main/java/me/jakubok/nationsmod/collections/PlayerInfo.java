package me.jakubok.nationsmod.collections;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.nbt.NbtCompound;

public class PlayerInfo implements ComponentV3 {

    public boolean inAWilderness = true;

    @Override
    public void readFromNbt(NbtCompound tag) {
        inAWilderness = tag.getBoolean("in_a_wilderness");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("in_a_wilderness", inAWilderness);
    }
    
}
