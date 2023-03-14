package me.jakubok.nationsmod.administration.abstractEntities;

import me.jakubok.nationsmod.administration.law.RuleType;
import me.jakubok.nationsmod.collections.Colour;

public class AdministratingUnitLawDescription extends LegalOrganisationLawDescription {
    public static final RuleDescription mapColour = new RuleDescription(RuleType.COLOUR, "The colour the territory claimer has on the map", false, new Colour(0));
    public static final String mapColourLabel = "mapColour";
    static {
        rulesDescriptions.put(mapColourLabel, mapColour);
    }
}
