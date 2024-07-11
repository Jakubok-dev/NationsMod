package me.jakubok.nationsmod.administration.abstractEntities;

import me.jakubok.nationsmod.administration.law.RuleType;
import net.minecraft.text.Text;

import java.util.UUID;

public abstract class TerritoryClaimerLawDescription extends LegalOrganisationLawDescription  {
    public static final RuleDescription shapesID = new RuleDescription(RuleType.UUID, Text.literal("Shape's ID"), "The id of the claimer's territory shape", false, UUID::randomUUID);
    public static final String shapesIDLabel = "shapesID";
    
    public TerritoryClaimerLawDescription() {
        this.rulesDescriptions.put(shapesIDLabel, shapesID);
    }
}
