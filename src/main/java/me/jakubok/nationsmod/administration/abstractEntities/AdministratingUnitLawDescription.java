package me.jakubok.nationsmod.administration.abstractEntities;

import me.jakubok.nationsmod.administration.law.LawApprovement;
import me.jakubok.nationsmod.administration.law.RuleType;
import me.jakubok.nationsmod.collections.Colour;

public class AdministratingUnitLawDescription extends LegalOrganisationLawDescription {
    public static final RuleDescription mapColour = new RuleDescription(RuleType.COLOUR, "The colour the territory claimer has on the map", false, new Colour(0));
    public static final String mapColourLabel = "mapColour";
    public static final RuleDescription citizenshipApprovement = new RuleDescription(RuleType.LAWAPPROVEMENT, "Which institutions need to consent in order for a person to get a citizenship.", false, LawApprovement.BY_LEGISLATIVE_OR_EXECUTIVE);
    public static final String citizenshipApprovementLabel = "citizenshipApprovement";
    public static final RuleDescription petitionSupport = new RuleDescription(RuleType.INTEGER, "The percentage of people in a nation which support a petition so that the petition must be deliberated by the government.", false, 10);
    public static final String petitionSupportLabel = "petitionSupport";
    static {
        rulesDescriptions.put(mapColourLabel, mapColour);
        rulesDescriptions.put(citizenshipApprovementLabel, citizenshipApprovement);
        rulesDescriptions.put(petitionSupportLabel, petitionSupport);
    }
}
