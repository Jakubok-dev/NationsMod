package me.jakubok.nationsmod.gui;

import com.google.common.collect.ImmutableList;
import me.jakubok.nationsmod.administration.abstractEntities.LegalOrganisation;
import me.jakubok.nationsmod.administration.law.Act;
import me.jakubok.nationsmod.administration.law.LawApprovement;
import me.jakubok.nationsmod.gui.miscellaneous.RadioButtonListWidget;
import me.jakubok.nationsmod.gui.miscellaneous.ResizableWindow;
import me.jakubok.nationsmod.gui.miscellaneous.form.InvalidFormWindow;
import me.jakubok.nationsmod.gui.miscellaneous.property.TextProperty;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Arrays;

public class ChangeLawApprovementScreen extends ResizableWindow {
    public RadioButtonListWidget widget;
    public ButtonWidget submit;
    public final Act<?> act;
    public final LegalOrganisation<?> organisation;
    public final String ruleLabel;

    public ChangeLawApprovementScreen(Text title, Screen previousScreen, Act<?> act, LegalOrganisation<?> organisation, String ruleLabel) {
        super(title, 280, 165, 4, previousScreen);
        this.act = act;
        this.organisation = organisation;
        this.ruleLabel = ruleLabel;
    }

    @Override
    protected void init() {
        super.init();
        this.widget = new RadioButtonListWidget(
                this.client,
                Arrays.stream(LawApprovement.values()).map(v -> v.displayText).toList(),
                this.getWindowLeft(),
                this.getWindowWidth() - 5,
                this.getWindowHeight() - 52,
                this.getWindowTop() + 25,
                this.getWindowBottom() - 27,
                25
        );
        int selected = this.act.getARule(this.ruleLabel) != null ? ((LawApprovement)this.act.getARule(this.ruleLabel)).value : ((LawApprovement)this.organisation.law.getARule(this.ruleLabel)).value;
        this.widget.setSelected(selected);
        this.addDrawableChild(widget);
        this.submit = ButtonWidget.builder(Text.of("Submit"), c -> {
            assert this.client != null;
            if (this.widget.getSelected() == -1) {
                this.client.setScreen(new InvalidFormWindow(
                        Text.of(this.title.getString() + " - " + "Invalid prompt"),
                        ImmutableList.of(new TextProperty(this.client, Text.of(this.organisation.law.toString(this.ruleLabel)), Text.literal("No choice selected").formatted(Formatting.RED))),
                        this.getWindowWidth(),
                        this.getWindowHeight(),
                        this.getBorderRadius(),
                        this
                ));
            }
            if (this.organisation.law.getARule(this.ruleLabel) != null) {
                if (this.organisation.law.getARule(this.ruleLabel).equals(LawApprovement.values()[this.widget.getSelected()])) {
                    this.act.resetARule(this.ruleLabel);
                    this.close();
                    return;
                }
            }
            this.act.putARule(this.ruleLabel, LawApprovement.values()[this.widget.getSelected()]);
            this.close();
        }).dimensions(
                this.getWindowLeft() + this.getWindowWidth() / 6,
                this.getWindowBottom() - 25,
                2 * this.getWindowWidth() / 3,
                20
        ).build();
        this.addDrawableChild(this.submit);
    }
}
