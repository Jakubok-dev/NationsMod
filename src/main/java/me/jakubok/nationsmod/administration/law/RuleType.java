package me.jakubok.nationsmod.administration.law;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import me.jakubok.nationsmod.collections.Colour;

public enum RuleType {
    INTEGER(obj -> obj instanceof Integer),
    STRING(obj -> obj instanceof String),
    DOUBLE(obj -> obj instanceof Double),
    LONG(obj -> obj instanceof Long),
    UUID(obj -> obj instanceof UUID),
    COLOUR(obj -> obj instanceof Colour),
    LISTOFUUID(obj -> obj instanceof List),
    SETOFUUID(obj -> obj instanceof Set),
    SETOFPLAYERACOUNT(obj -> obj instanceof Set),
    LAWAPPROVEMENT(obj -> obj instanceof LawApprovement);

    private final Function<Object, Boolean> isInstance;
    RuleType(Function<Object, Boolean> isInstance) {
        this.isInstance = isInstance;
    }

    public boolean fits(Object obj) {
        return isInstance.apply(obj);
    }
}
