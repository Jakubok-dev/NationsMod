package me.jakubok.nationsmod.administration;

import java.util.ArrayList;
import java.util.UUID;

public class ProvinceLawDescription extends TerritoryClaimerLawDescription {
    public static final RuleDescription nationsID = new RuleDescription(RuleType.UUID, "The UUID of the nation the province belongs to", true, null);
    public static final String nationsIDLabel = "nationsID";
    public static final RuleDescription capitalsID = new RuleDescription(RuleType.UUID, "The UUID of the province's capital", true, null);
    public static final String capitalsIDLabel = "capitalsID";
    public static final RuleDescription townsIDs = new RuleDescription(RuleType.LISTOFUUID, "The UUIDs of towns the province has", false, new ArrayList<UUID>());
    public static final String townsIDsLabel = "townsIDs";
    
    static {
        rulesDescriptions.put(nationsIDLabel, nationsID);
        rulesDescriptions.put(capitalsIDLabel, capitalsID);
        rulesDescriptions.put(townsIDsLabel, townsIDs);
    }
}
