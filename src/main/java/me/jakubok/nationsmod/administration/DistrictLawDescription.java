package me.jakubok.nationsmod.administration;

public class DistrictLawDescription extends TerritoryClaimerLawDescription {
    public static final RuleDescription townID = new RuleDescription(RuleType.UUID, "The uuid of a town the district belongs to", null);
    public static final String townIDLabel = "townID";

    static {
        rulesDescriptions.put(townIDLabel, townID);
    }
}
