package me.jakubok.nationsmod.administration.abstractEntities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.jakubok.nationsmod.administration.law.LawDescription;
import me.jakubok.nationsmod.administration.law.RuleType;
import net.minecraft.text.Text;

public abstract class LegalOrganisationLawDescription implements LawDescription {

    public static final RuleDescription Name = new RuleDescription(RuleType.STRING, Text.literal("Name"), "The name of the administrating unit", true, () -> null);
    public static final String NameLabel = "name";
    public static final RuleDescription Id = new RuleDescription(RuleType.UUID, Text.literal("ID"), "The UUID of the administrating unit", true, UUID::randomUUID);
    public static final String IdLabel = "id";

    protected static final Map<String, RuleDescription> rulesDescriptions = new HashMap<>() {{
        put(NameLabel, Name);
        put(IdLabel, Id);
    }};

    @Override
    public Map<String, RuleDescription> getRulesDescriptions() {
        return rulesDescriptions;
    }
    
}
