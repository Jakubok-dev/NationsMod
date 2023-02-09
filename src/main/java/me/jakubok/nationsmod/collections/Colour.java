package me.jakubok.nationsmod.collections;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.nbt.NbtCompound;

public class Colour implements ComponentV3 {
    private int r, g, b;
    public Colour(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    public Colour(int bitmask) {
        this.r = (bitmask >> 16) & 0xFF;
        this.g = (bitmask >> 8) & 0xFF;
        this.b = bitmask & 0xFF;
    }
    public Colour(NbtCompound compound) {
        this.readFromNbt(compound);
    }

    public int getR() {
        return r;
    }
    public int getG() {
        return g;
    }
    public int getB() {
        return b;
    }

    public void setR(int r) {
        if (r > 255) return;
        this.r = r;
    }
    public void setG(int g) {
        if (g > 255) return;
        this.g = g;
    }
    public void setB(int b) {
        if (b > 255) return;
        this.b = b;
    }

    public int getBitmask() {
        return (((r << 8) + g) << 8) + b;
    }

    public void mix(Colour c, double factor) {
        this.r = (int)(((double)this.r + (double)c.r * factor) / (factor + 1));
        this.g = (int)(((double)this.g + (double)c.g * factor) / (factor + 1));
        this.b = (int)(((double)this.b + (double)c.b * factor) / (factor + 1));
    }

    public void changeTheShade(double factor) {
        this.r = (int)((double)this.r * factor);
        if (this.r > 255)
            this.r = 255;
        if (this.r < 0)
            this.r = 0;
        this.g = (int)((double)this.g * factor);
        if (this.g > 255)
            this.g = 255;
        if (this.g < 0)
            this.g = 0;
        this.b = (int)((double)this.b * factor);
        if (this.b > 255)
            this.b = 255;
        if (this.b < 0)
            this.b = 0;
    }

    @Override
    public int hashCode() {
        return this.getBitmask();
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }

    public static int GET_BITMASK(int r, int g, int b) {
        return (((r << 8) + g) << 8) + b;
    }

    public static Colour MIX(Colour firstColour, double firstFactor, Colour secondColour, double secondFactor) {
        return new Colour(
            (int)(((double)firstColour.r*firstFactor + (double)secondColour.r*secondFactor) / (firstFactor + secondFactor)),
            (int)(((double)firstColour.g*firstFactor + (double)secondColour.g*secondFactor) / (firstFactor + secondFactor)),
            (int)(((double)firstColour.b*firstFactor + (double)secondColour.b*secondFactor) / (firstFactor + secondFactor))
        );
    }
    @Override
    public void readFromNbt(NbtCompound tag) {
        int bitmask = tag.getInt("colour");
        this.r = (bitmask >> 16) & 0xFF;
        this.g = (bitmask >> 8) & 0xFF;
        this.b = bitmask & 0xFF;
    }
    @Override
    public void writeToNbt(NbtCompound tag) {
        writeToNbtAndReturn(tag);
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        tag.putInt("colour", this.getBitmask());
        return tag;
    }
}
