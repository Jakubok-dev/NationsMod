package me.jakubok.nationsmod.administration.town;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import me.jakubok.nationsmod.administration.abstractEntities.AdministratingUnitLawDescription;
import me.jakubok.nationsmod.administration.law.RuleType;
import me.jakubok.nationsmod.collections.PlayerAccount;

public class TownLawDescription extends AdministratingUnitLawDescription{
    public static final RuleDescription provincesID = new RuleDescription(RuleType.UUID, "The id of the province in which the town is", true, null);
    public static final String provincesIDLabel = "provincesId"; 
    public static final RuleDescription listOfDistrictsIDs = new RuleDescription(RuleType.LISTOFUUID, "The list of UUIDs of districts which belong to the town", false, new ArrayList<UUID>());
    public static final String listOfDistrictsIDsLabel = "listOfDistrictsIDs"; 
    public static final RuleDescription setOfPlayerMembers = new RuleDescription(RuleType.SETOFPLAYERACOUNT, "The accounts of citizens belonigng to the town", false, new HashSet<PlayerAccount>());
    public static final String setOfPlayerMembersLabel = "setOfPlayerMembers";
    public static final RuleDescription setOfAIMembers = new RuleDescription(RuleType.SETOFUUID, "The UUIDs of citizens belonigng to the town", false, new HashSet<UUID>());
    public static final String setOfAIMembersLabel = "setOfAIMembers"; 
    static {
        rulesDescriptions.put(provincesIDLabel, provincesID);
        rulesDescriptions.put(listOfDistrictsIDsLabel, listOfDistrictsIDs);
        rulesDescriptions.put(setOfPlayerMembersLabel, setOfPlayerMembers);
        rulesDescriptions.put(setOfAIMembersLabel, setOfAIMembers);
    }
}
