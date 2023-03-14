package me.jakubok.nationsmod.administration.abstractEntities;

import me.jakubok.nationsmod.administration.law.RuleType;

public abstract class TerritoryClaimerLawDescription extends LegalOrganisationLawDescription  {
    public static final RuleDescription claimedBlocksCount = new RuleDescription(RuleType.LONG, "The total amount of blocks the territory claimer has", false, Long.valueOf(0));
    public static final String claimedBlocksCountLabel = "claimedBlocksCount";
    public static final RuleDescription minX = new RuleDescription(RuleType.INTEGER, "The most west part of the claimer", false, -2147483648);
    public static final String minXLabel = "minX";
    public static final RuleDescription maxX = new RuleDescription(RuleType.INTEGER, "The most east part of the claimer", false, 2147483647);
    public static final String maxXLabel = "maxX";
    public static final RuleDescription minZ = new RuleDescription(RuleType.INTEGER, "The most north part of the claimer", false, -2147483648);
    public static final String minZLabel = "minZ";
    public static final RuleDescription maxZ = new RuleDescription(RuleType.INTEGER, "The most south part of the claimer", false, 2147483647);
    public static final String maxZLabel = "maxZ";
    
    static {
        rulesDescriptions.put(claimedBlocksCountLabel, claimedBlocksCount);
        rulesDescriptions.put(minXLabel, minX);
        rulesDescriptions.put(maxXLabel, maxX);
        rulesDescriptions.put(minZLabel, minZ);
        rulesDescriptions.put(maxZLabel, maxZ);
    }
}
