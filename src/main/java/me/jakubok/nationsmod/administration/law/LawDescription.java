package me.jakubok.nationsmod.administration.law;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;

public interface LawDescription {

    Map<String, RuleDescription> getRulesDescriptions();

    class RuleDescription {
        protected final RuleType type;
        protected final Text displayName;
        protected final String description;
        public final Supplier<Object> defaultValue;
        public final Boolean nullable;
        public final GetOnChangeMessage getOnChangeMessage;

        public RuleDescription(RuleType type, Text displayName, String description, Boolean nullable, Supplier<Object> defaultValue) {
            this(type, displayName, description, nullable, defaultValue, (oldValue, newValue, renderer, width) -> {
                Text message = Text.literal("Change " + displayName.getString() + " from " + oldValue + " to " + newValue);
                return renderer.wrapLines(message, width);
            });
        }
        public RuleDescription(RuleType type, Text displayName, String description, Boolean nullable, Supplier<Object> defaultValue, GetOnChangeMessage getOnChangeMessage) {
            this.type = type;
            this.displayName = displayName;
            this.description = description;
            this.nullable = nullable;
            if (this.compatible(defaultValue.get()))
                this.defaultValue = defaultValue;
            else this.defaultValue = () -> null;
            this.getOnChangeMessage = getOnChangeMessage;
        }

        public boolean compatible(Object obj) {
            if (this.nullable && obj == null)
                return true;
            if (!this.type.fits(obj))
                throw new CrashException(this.createCrashReport(new ClassCastException("Class cast exception"), obj));
            return true;
        }

        private CrashReport createCrashReport(ClassCastException exception, Object obj) {
            return CrashReport.create(exception, "Tried to set a rule with a value which type is not compatible with the rule. \nThe expected type: " + this.type.name() + " \nTried to set: " + obj);
        }
    }

    @FunctionalInterface
    interface GetOnChangeMessage {
        List<OrderedText> get(String oldValue, String newValue, TextRenderer renderer, int width);
    }
}
