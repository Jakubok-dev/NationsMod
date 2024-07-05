package me.jakubok.nationsmod.administration.law;

import java.util.Map;

import me.jakubok.nationsmod.administration.law.LawDescription.RuleDescription;
import net.minecraft.nbt.NbtCompound;

public class Law<D extends LawDescription> extends LawHolder<D> {
    public Law(D description) {
        super(description);
        this.initValues();
    }
    public Law(D description, NbtCompound nbt) {
        super(description, nbt);
        this.initValues();
    }

    protected void initValues() {
        for (Map.Entry<String, RuleDescription> ruleDescription : this.description.getRulesDescriptions().entrySet()) {
            if (this.getARule(ruleDescription.getKey()) == null)
                this.putARule(ruleDescription.getKey(), ruleDescription.getValue().defaultValue.get());
        }
    }
}
