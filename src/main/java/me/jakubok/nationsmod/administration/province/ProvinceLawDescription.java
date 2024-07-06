package me.jakubok.nationsmod.administration.province;

import java.util.ArrayList;
import java.util.UUID;

import me.jakubok.nationsmod.administration.abstractEntities.TerritoryClaimerLawDescription;
import me.jakubok.nationsmod.administration.law.RuleType;
import net.minecraft.text.Text;

public class ProvinceLawDescription extends TerritoryClaimerLawDescription {
    public static final RuleDescription nationsID = new RuleDescription(RuleType.UUID, Text.literal("Nation's ID"), "The UUID of the nation the province belongs to", true, () -> null);
    public static final String nationsIDLabel = "nationsID";

    static {
        rulesDescriptions.put(nationsIDLabel, nationsID);
    }
}
