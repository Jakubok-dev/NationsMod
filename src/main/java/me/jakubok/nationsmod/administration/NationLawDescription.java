package me.jakubok.nationsmod.administration;

import java.util.ArrayList;
import java.util.UUID;

public class NationLawDescription extends LegalOrganisationLawDescription {
    public static final RuleDescription capitalsID = new RuleDescription(RuleType.UUID, "The id of the nation's capital", null);
    public static final String capitalsIDLabel = "capitalsId"; 
    public static final RuleDescription listOfProvincesIDs = new RuleDescription(RuleType.LISTOFUUID, "IDs of provinces which belong to the nation", new ArrayList<UUID>());
    public static final String listOfProvincesIDsLabel = "listOfProvincesID"; 
    static {
        rulesDescriptions.put(capitalsIDLabel, capitalsID);
        rulesDescriptions.put(listOfProvincesIDsLabel, listOfProvincesIDs);
    }
}
