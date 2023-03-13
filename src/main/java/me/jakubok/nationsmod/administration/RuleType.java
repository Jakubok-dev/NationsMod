package me.jakubok.nationsmod.administration;

public enum RuleType {
    INTEGER("java.lang.Integer"),
    STRING("java.lang.String"),
    DOUBLE("java.lang.Double"),
    LONG("java.lang.Long"),
    UUID("java.util.UUID"),
    COLOUR("me.jakubok.nationsmod.collections.Colour"),
    LISTOFUUID("java.util.List"),
    SETOFUUID("java.util.Set"),
    SETOFPLAYERACOUNT("java.util.Set");

    private String cl;
    RuleType(String cl) {
        this.cl = cl;
    }

    public boolean fits(Object obj) throws ClassNotFoundException {
        return Class.forName(cl).isInstance(obj);
    }
}
