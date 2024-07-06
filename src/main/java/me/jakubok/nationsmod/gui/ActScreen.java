package me.jakubok.nationsmod.gui;

import me.jakubok.nationsmod.administration.abstractEntities.LegalOrganisation;
import me.jakubok.nationsmod.administration.abstractEntities.LegalOrganisationLawDescription;
import me.jakubok.nationsmod.administration.law.Act;
import me.jakubok.nationsmod.administration.nation.Nation;
import me.jakubok.nationsmod.administration.nation.NationLawDescription;
import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.administration.town.TownLawDescription;
import me.jakubok.nationsmod.gui.miscellaneous.ResizableWindow;
import me.jakubok.nationsmod.gui.miscellaneous.TextEntry;
import me.jakubok.nationsmod.gui.miscellaneous.property.PropertyEntry;
import me.jakubok.nationsmod.gui.miscellaneous.property.PropertyListWidget;
import me.jakubok.nationsmod.gui.townScreen.TownScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class ActScreen<T extends LegalOrganisationLawDescription> extends ResizableWindow {
    public ButtonWidget edit, submit, close;
    public Act<T> act;
    public LegalOrganisation<T> organisation;
    public PropertyListWidget list;
    List<PropertyEntry> text = new ArrayList<>();
    public ActScreen(Act<T> act, LegalOrganisation<T> organisation, Screen previousScreen) {
        super(Text.literal(act.getName()).formatted(Formatting.BOLD), 170, 200, 4, previousScreen);
        this.act = act;
        this.organisation = organisation;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void init() {
        super.init();
        assert this.client != null;
        for (String ruleName : this.act.existingRules()) {
            List<TextEntry> message = this.act.description.getRulesDescriptions().get(ruleName).getOnChangeMessage.get(
                    this.organisation.law.toString(ruleName),
                    this.act.toString(ruleName),
                    this.client.textRenderer,
                    this.getWindowWidth() - 10
            ).stream().map(t -> new TextEntry(this.client, t)).toList();
            this.text.addAll(message);
        }

        if (this.text.isEmpty()) {
            this.text.add(new TextEntry(this.client, Text.literal("The act is empty").asOrderedText()));
        }
        this.windowHeight = Math.min(this.windowHeight, 65 + 15 * this.text.size());

        this.edit = ButtonWidget.builder(
                Text.of("Edit"),
                b -> {
                    if (act.description instanceof TownLawDescription) {
                        Town town = (Town)this.organisation;
                        assert this.client != null;
                        this.client.setScreen(new TownScreen(town, this, (Act<TownLawDescription>) this.act));
                        return;
                    }
                    if (act.description instanceof NationLawDescription) {
                        Nation nation = (Nation) this.organisation;
                    }
                }
        ).dimensions(
                this.getWindowLeft() + 5,
                this.getWindowBottom() - 25,
                (this.getWindowWidth() - 10) / 3 - 5,
                20
        ).build();
        this.addDrawableChild(this.edit);
        this.submit = ButtonWidget.builder(
                Text.of("Submit"),
                b -> {

                }
        ).dimensions(
                this.getWindowLeft() + 5 + (this.getWindowWidth() - 10) / 3,
                this.getWindowBottom() - 25,
                (this.getWindowWidth() - 10) / 3 - 5,
                20
        ).build();
        this.addDrawableChild(this.submit);
        this.close = ButtonWidget.builder(
                Text.of("Close"),
                b -> this.close()
        ).dimensions(
                this.getWindowLeft() + 5 + 2 * ((this.getWindowWidth() - 10) / 3),
                this.getWindowBottom() - 25,
                (this.getWindowWidth() - 10) / 3,
                20
        ).build();
        this.addDrawableChild(this.close);

        this.list = new PropertyListWidget(
                this.getClient(),
                this.text,
                this.getWindowLeft() + 5,
                this.getWindowWidth() - 10,
                this.getWindowHeight() - 60,
                this.getWindowTop() + 30,
                this.getWindowBottom() - 30,
                15
        );
        this.addDrawableChild(this.list);
    }
}
