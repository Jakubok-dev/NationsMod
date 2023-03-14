package me.jakubok.nationsmod.administration.nation;

import java.util.ArrayList;
import java.util.UUID;

import me.jakubok.nationsmod.administration.abstractEntities.AdministratingUnitLawDescription;
import me.jakubok.nationsmod.administration.law.RuleType;

public class NationLawDescription extends AdministratingUnitLawDescription {
    public static final RuleDescription capitalsID = new RuleDescription(RuleType.UUID, "The id of the nation's capital", true, null);
    public static final String capitalsIDLabel = "capitalsId"; 
    public static final RuleDescription listOfProvincesIDs = new RuleDescription(RuleType.LISTOFUUID, "IDs of provinces which belong to the nation", false, new ArrayList<UUID>());
    public static final String listOfProvincesIDsLabel = "listOfProvincesID"; 
    static {
        rulesDescriptions.put(capitalsIDLabel, capitalsID);
        rulesDescriptions.put(listOfProvincesIDsLabel, listOfProvincesIDs);
    }
}
