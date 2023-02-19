package me.jakubok.nationsmod.administration;

import java.util.ArrayList;
import java.util.UUID;

import me.jakubok.nationsmod.collections.PlayerAccount;

public class TownLawDescription extends AdministratingUnitLawDescription{
    public static final RuleDescription provincesID = new RuleDescription(RuleType.UUID, "The id of the province in which the town is", null);
    public static final String provincesIDLabel = "provincesId"; 
    public static final RuleDescription listOfDistrictsIDs = new RuleDescription(RuleType.LISTOFUUID, "The list of UUIDs of districts which belong to the town", new ArrayList<UUID>());
    public static final String listOfDistrictsIDsLabel = "listOfDistrictsIDs"; 
    public static final RuleDescription listOfPlayerAccounts = new RuleDescription(RuleType.LISTOFPLAYERACOUNT, "The accounts of citizens belonigng to the town", new ArrayList<PlayerAccount>());
    public static final String listOfPlayerAccountsLabel = "listOfPlayerAccounts"; 
    static {
        rulesDescriptions.put(provincesIDLabel, provincesID);
        rulesDescriptions.put(listOfDistrictsIDsLabel, listOfDistrictsIDs);
        rulesDescriptions.put(listOfPlayerAccountsLabel, listOfPlayerAccounts);
    }
}
