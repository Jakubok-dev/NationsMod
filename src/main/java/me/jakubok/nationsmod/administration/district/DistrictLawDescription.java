package me.jakubok.nationsmod.administration.district;

import me.jakubok.nationsmod.administration.abstractEntities.TerritoryClaimerLawDescription;
import me.jakubok.nationsmod.administration.law.RuleType;

public class DistrictLawDescription extends TerritoryClaimerLawDescription {
    public static final RuleDescription townID = new RuleDescription(RuleType.UUID, "The uuid of a town the district belongs to", true, null);
    public static final String townIDLabel = "townID";

    static {
        rulesDescriptions.put(townIDLabel, townID);
    }
}
