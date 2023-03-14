package me.jakubok.nationsmod.administration.abstractEntities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.jakubok.nationsmod.administration.law.LawDescription;
import me.jakubok.nationsmod.administration.law.RuleType;

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
