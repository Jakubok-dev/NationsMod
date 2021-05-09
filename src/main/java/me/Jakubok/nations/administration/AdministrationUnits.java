package me.Jakubok.nations.administration;

public enum AdministrationUnits {

    TOWN(2),
    COUNTRY(4);

    AdministrationUnits(int level) {
        this.level = level;
    }

    public final int level;

}
