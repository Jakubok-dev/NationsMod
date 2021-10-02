package me.jakubok.nationsmod.collections;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class Border implements ComponentV3 {

    public BlockPos position;

    public Border(int x, int z) {
        this.position = new BlockPos(x, 64, z);
    }
    public Border(BlockPos pos) {
        this(pos.getX(), pos.getZ());
    }
    public Border() {
        this.position = new BlockPos(0, 0, 0);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        int[] positionArray = tag.getIntArray("position");
        this.position = new BlockPos(positionArray[0], positionArray[1], positionArray[2]);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putIntArray("position", new int[] { this.position.getX(), this.position.getY(), this.position.getZ() });
    }
    
}
