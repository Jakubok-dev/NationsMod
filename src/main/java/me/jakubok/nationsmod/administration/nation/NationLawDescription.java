package me.jakubok.nationsmod.administration.nation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import me.jakubok.nationsmod.administration.abstractEntities.AdministratingUnitLawDescription;
import me.jakubok.nationsmod.administration.law.RuleType;

public class NationLawDescription extends AdministratingUnitLawDescription {
    public static final RuleDescription capitalsID = new RuleDescription(RuleType.UUID, "The id of the nation's capital", true, () -> null);
    public static final String capitalsIDLabel = "capitalsId"; 
    public static final RuleDescription listOfProvincesIDs = new RuleDescription(RuleType.LISTOFUUID, "IDs of provinces which belong to the nation", false, ArrayList<UUID>::new);
    public static final String listOfProvincesIDsLabel = "listOfProvincesID";
    public static final RuleDescription listOfTownsIDs = new RuleDescription(RuleType.LISTOFUUID, "IDs of towns which belong to the nation", false, ArrayList<UUID>::new);
    public static final String listOfTownsIDsLabel = "listOfTownsIDs";
    public static final RuleDescription townProvinceRegistry = new RuleDescription(RuleType.MAPOFUUIDS, "Contains the UUID of a town and a UUID of a province it belongs to", false, HashMap<UUID, UUID>::new);
    public static final String townProvinceRegistryLabel = "townProvinceRegistry";
    public static final RuleDescription provincesCapitalRegistry = new RuleDescription(RuleType.MAPOFUUIDS, "Contains the UUID of a province and a UUID of a town which is province's capital", false, HashMap<UUID, UUID>::new);
    public static final String provincesCapitalRegistryLabel = "provincesCapitalRegistry";
    static {
        rulesDescriptions.put(capitalsIDLabel, capitalsID);
        rulesDescriptions.put(listOfProvincesIDsLabel, listOfProvincesIDs);
        rulesDescriptions.put(listOfTownsIDsLabel, listOfTownsIDs);
        rulesDescriptions.put(townProvinceRegistryLabel, townProvinceRegistry);
        rulesDescriptions.put(provincesCapitalRegistryLabel, provincesCapitalRegistry);
    }
}
