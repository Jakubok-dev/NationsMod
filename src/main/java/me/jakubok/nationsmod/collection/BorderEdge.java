package me.jakubok.nationsmod.collection;

import me.jakubok.nationsmod.geometry.LinearFunction;

import java.util.UUID;

// Don't mistake it with the Border, BorderGroup and the BorderSlots pls!!!
public class BorderEdge {
    public final LinearFunction fun;
    public final UUID claimantsID;
    public final boolean startsTheShape;

    public BorderEdge(LinearFunction fun, UUID claimantsID, boolean startsTheShape) {
        this.fun = fun;
        this.claimantsID = claimantsID;
        this.startsTheShape = startsTheShape;
    }
}
