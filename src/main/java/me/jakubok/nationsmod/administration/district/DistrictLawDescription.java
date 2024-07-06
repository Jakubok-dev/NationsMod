package me.jakubok.nationsmod.administration.district;

import me.jakubok.nationsmod.administration.abstractEntities.TerritoryClaimerLawDescription;
import me.jakubok.nationsmod.administration.law.RuleType;
import net.minecraft.text.Text;

public class DistrictLawDescription extends TerritoryClaimerLawDescription {
    public static final RuleDescription townID = new RuleDescription(RuleType.UUID, Text.literal("Town's ID"), "The uuid of a town the district belongs to", true, () -> null);
    public static final String townIDLabel = "townID";

    static {
        rulesDescriptions.put(townIDLabel, townID);
    }
}
