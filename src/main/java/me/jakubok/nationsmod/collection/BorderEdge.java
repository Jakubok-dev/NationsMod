package me.jakubok.nationsmod.collection;

import me.jakubok.nationsmod.geometry.LinearFunction;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

// Don't mistake it with the Border, BorderGroup and the BorderSlots pls!!!
public class BorderEdge {
    public final LinearFunction fun;
    public final UUID shapesID;
    public final boolean startsTheShape;

    public BorderEdge(LinearFunction fun, UUID shapesID, boolean startsTheShape) {
        this.fun = fun;
        this.shapesID = shapesID;
        this.startsTheShape = startsTheShape;
    }
    public BorderEdge(NbtCompound nbt) {
        this.fun = LinearFunction.readFromNbt(nbt.getCompound("function"));
        this.shapesID = nbt.getUuid("shapesID");
        this.startsTheShape = nbt.getBoolean("startsTheShape");
    }

    public boolean collides(BorderEdge e) {
        if (this.fun.getTheIntersectionX(e.fun) != null)
            return true;
        if (this.fun.doesOverlap(e.fun))
            return this.startsTheShape == e.startsTheShape;
        return false;
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        NbtCompound functionSubtag = new NbtCompound();
        fun.writeToNbt(functionSubtag);
        tag.put("function", functionSubtag);
        tag.putUuid("shapesID", this.shapesID);
        tag.putBoolean("startsTheShape", this.startsTheShape);
        return tag;
    }
}
