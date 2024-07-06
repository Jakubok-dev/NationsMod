package me.jakubok.nationsmod.administration.town;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import me.jakubok.nationsmod.administration.abstractEntities.AdministratingUnitLawDescription;
import me.jakubok.nationsmod.administration.law.RuleType;
import me.jakubok.nationsmod.collection.PlayerAccount;
import net.minecraft.text.Text;

public class TownLawDescription extends AdministratingUnitLawDescription{

    public static final RuleDescription nationsID = new RuleDescription(RuleType.UUID, Text.literal("Nation's ID"), "The id of the nation the town is in", true, () -> null);
    public static final String nationsIDLabel = "nationId";
    public static final RuleDescription listOfDistrictsIDs = new RuleDescription(RuleType.LISTOFUUID, Text.literal("Districts' IDs"), "The list of UUIDs of districts which belong to the town", false, ArrayList<UUID>::new);
    public static final String listOfDistrictsIDsLabel = "listOfDistrictsIDs"; 
    public static final RuleDescription setOfPlayerMembers = new RuleDescription(RuleType.SETOFPLAYERACOUNT, Text.literal("Player members"), "The accounts of citizens belonigng to the town", false, HashSet<PlayerAccount>::new);
    public static final String setOfPlayerMembersLabel = "setOfPlayerMembers";
    public static final RuleDescription setOfAIMembers = new RuleDescription(RuleType.SETOFUUID, Text.literal("NPC members"), "The UUIDs of citizens belonigng to the town", false, HashSet<UUID>::new);
    public static final String setOfAIMembersLabel = "setOfAIMembers"; 
    static {
        rulesDescriptions.put(nationsIDLabel, nationsID);
        rulesDescriptions.put(listOfDistrictsIDsLabel, listOfDistrictsIDs);
        rulesDescriptions.put(setOfPlayerMembersLabel, setOfPlayerMembers);
        rulesDescriptions.put(setOfAIMembersLabel, setOfAIMembers);
    }
}
