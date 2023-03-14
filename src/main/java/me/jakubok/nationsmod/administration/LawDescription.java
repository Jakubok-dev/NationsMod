package me.jakubok.nationsmod.administration;

import java.util.Map;

import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;

public interface LawDescription {

    public Map<String, RuleDescription> getRulesDescriptions();

    public class RuleDescription {
        protected final RuleType type;
        protected final String description;
        public final Object defaultValue;
        public final Boolean nullable;

        public RuleDescription(RuleType type, String description, Boolean nullable, Object defaultValue) {
            this.type = type;
            this.description = description;
            this.nullable = nullable;
            if (this.compatible(defaultValue))
                this.defaultValue = defaultValue;
            else this.defaultValue = null;
        }

        public boolean compatible(Object obj) {
            if (this.nullable && obj == null)
                return true;
            if (!this.type.fits(obj))
                throw new CrashException(this.createCrashReport(new ClassCastException("Class cast exception"), obj));
            return true;
        }

        private CrashReport createCrashReport(ClassCastException exception, Object obj) {
            CrashReport crashReport = CrashReport.create(exception, "Tried to set a rule with a value which type is not compatible with the rule. \nThe expected type: " + this.type.name() + " \nTried to set: " + obj);
            return crashReport;
        }
    }
}
