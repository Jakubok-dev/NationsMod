package me.jakubok.nationsmod.administration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class LegalOrganisationLawDescription implements LawDescription {

    public static final RuleDescription Name = new RuleDescription(RuleType.STRING, "The name of the administrating unit", true, null);
    public static final String NameLabel = "name";
    public static final RuleDescription Id = new RuleDescription(RuleType.UUID, "The UUID of the administrating unit", true, UUID.randomUUID());
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
