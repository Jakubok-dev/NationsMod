package me.jakubok.nationsmod.gui.miscellaneous.property;

import com.google.common.collect.ImmutableList;
import me.jakubok.nationsmod.administration.abstractEntities.LegalOrganisation;
import me.jakubok.nationsmod.administration.law.Act;
import me.jakubok.nationsmod.gui.miscellaneous.form.FormWindow;
import me.jakubok.nationsmod.gui.miscellaneous.form.TextInput;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ChangeATextPropertyScreen extends FormWindow {
    public final Text label;
    public final MutableText placeholder;
    public final Act<?> act;
    public final String ruleLabel;
    public final Function<Object, Text> validateFunction;
    public final LegalOrganisation<?> organisation;
    public final Consumer<List<Object>> onSubmit;

    public ChangeATextPropertyScreen(Text title, int width, int height, int borderRadius, Screen previousScreen, Text label, MutableText placeholder, Act<?> act, LegalOrganisation<?> organisation, String ruleLabel, Function<Object, Text> validateFunction) {
        this(title, width, height, borderRadius, previousScreen, label, placeholder, act, organisation, ruleLabel, validateFunction, null);
    }
    public ChangeATextPropertyScreen(Text title, int width, int height, int borderRadius, Screen previousScreen, Text label, MutableText placeholder, Act<?> act, LegalOrganisation<?> organisation, String ruleLabel, Function<Object, Text> validateFunction, Consumer<List<Object>> onSubmit) {
        super(title, width, height, borderRadius, previousScreen);
        this.label = label;
        this.placeholder = placeholder;
        this.act = act;
        this.organisation = organisation;
        this.ruleLabel = ruleLabel;
        this.validateFunction = validateFunction;
        this.onSubmit = onSubmit != null ? onSubmit : l -> {
            if (this.organisation.law.getARule(this.ruleLabel) != null) {
                if (this.organisation.law.getARule(this.ruleLabel).equals(l.get(0))) {
                    this.act.resetARule(this.ruleLabel);
                    this.close();
                    return;
                }
            }
            this.act.putARule(this.ruleLabel, l.get(0));
            this.close();
        };
    }

    @Override
    protected void init() {
        this.fillInTheDescription(
                ImmutableList.of(
                    new TextInput(
                            this.label,
                            this.placeholder,
                            this.validateFunction,
                            this.client
                    )
                ),
                this.onSubmit
        );
        super.init();
    }
}
