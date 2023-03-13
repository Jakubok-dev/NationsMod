package me.jakubok.nationsmod.administration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import me.jakubok.nationsmod.collections.PlayerAccount;

public class TownLawDescription extends AdministratingUnitLawDescription{
    public static final RuleDescription provincesID = new RuleDescription(RuleType.UUID, "The id of the province in which the town is", null);
    public static final String provincesIDLabel = "provincesId"; 
    public static final RuleDescription listOfDistrictsIDs = new RuleDescription(RuleType.LISTOFUUID, "The list of UUIDs of districts which belong to the town", new ArrayList<UUID>());
    public static final String listOfDistrictsIDsLabel = "listOfDistrictsIDs"; 
    public static final RuleDescription setOfPlayerMembers = new RuleDescription(RuleType.SETOFPLAYERACOUNT, "The accounts of citizens belonigng to the town", new HashSet<PlayerAccount>());
    public static final String setOfPlayerMembersLabel = "setOfPlayerMembers";
    public static final RuleDescription setOfAIMembers = new RuleDescription(RuleType.SETOFUUID, "The accounts of citizens belonigng to the town", new HashSet<UUID>());
    public static final String setOfAIMembersLabel = "setOfAIMembers"; 
    static {
        rulesDescriptions.put(provincesIDLabel, provincesID);
        rulesDescriptions.put(listOfDistrictsIDsLabel, listOfDistrictsIDs);
        rulesDescriptions.put(setOfPlayerMembersLabel, setOfPlayerMembers);
        rulesDescriptions.put(setOfAIMembersLabel, setOfAIMembers);
    }
}
