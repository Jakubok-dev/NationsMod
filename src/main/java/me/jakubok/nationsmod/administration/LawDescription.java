package me.jakubok.nationsmod.administration;

import java.util.Map;

public interface LawDescription {

    public Map<String, RuleDescription> getRulesDescriptions();

    public class RuleDescription {
        protected final RuleType type;
        protected final String description;
        public final Object defaultValue;

        public RuleDescription(RuleType type, String description, Object defaultValue) {
            this.type = type;
            this.description = description;
            if (this.compatible(defaultValue))
                this.defaultValue = defaultValue;
            else this.defaultValue = null;
        }

        public boolean compatible(Object obj) {
            try {
                return this.type.fits(obj);
            } catch(Exception ex) {
                return false;
            }
        }
    }
}
