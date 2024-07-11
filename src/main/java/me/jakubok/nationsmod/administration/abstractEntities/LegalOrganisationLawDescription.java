package me.jakubok.nationsmod.administration.abstractEntities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.jakubok.nationsmod.administration.law.LawDescription;
import me.jakubok.nationsmod.administration.law.Order;
import me.jakubok.nationsmod.administration.law.RuleType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public abstract class LegalOrganisationLawDescription implements LawDescription {

    public static final RuleDescription Name = new RuleDescription(
            RuleType.STRING,
            Text.literal("Name"),
            "The name of the administrating unit",
            true, () -> null
    );
    public static final String NameLabel = "name";
    public static final RuleDescription Id = new RuleDescription(
            RuleType.UUID,
            Text.literal("ID"),
            "The UUID of the administrating unit",
            true,
            UUID::randomUUID
    );
    public static final String IdLabel = "id";

    public static final Order DisbandOrder = new Order(
            Text.literal("Disband"),
            Text.literal("Disband the organisation"),
            (context, organisation, renderer, width) -> {
                Text message = Text.literal("Disband " + organisation.getName()).formatted(Formatting.BOLD).formatted(Formatting.RED);
                return renderer.wrapLines(message, width);
            },
            (context, organisation, server) -> organisation.deregister(server)
    );
    public static final String DisbandOrderLabel = "disbandOrder";

    protected final Map<String, RuleDescription> rulesDescriptions = new HashMap<>() {{
        put(NameLabel, Name);
        put(IdLabel, Id);
    }};

    protected final Map<String, Order> orders = new HashMap<>() {{
        put(DisbandOrderLabel, DisbandOrder);
    }};

    @Override
    public Map<String, RuleDescription> getRulesDescriptions() {
        return rulesDescriptions;
    }

    @Override
    public Map<String, Order> getOrders() {
        return orders;
    }
}
