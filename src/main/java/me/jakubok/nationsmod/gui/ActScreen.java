package me.jakubok.nationsmod.gui;

import com.google.common.collect.ImmutableList;
import me.jakubok.nationsmod.administration.abstractEntities.LegalOrganisation;
import me.jakubok.nationsmod.administration.abstractEntities.LegalOrganisationLawDescription;
import me.jakubok.nationsmod.administration.law.Act;
import me.jakubok.nationsmod.administration.law.Order;
import me.jakubok.nationsmod.administration.nation.Nation;
import me.jakubok.nationsmod.administration.nation.NationLawDescription;
import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.administration.town.TownLawDescription;
import me.jakubok.nationsmod.gui.miscellaneous.ResizableWindow;
import me.jakubok.nationsmod.gui.miscellaneous.TextEntry;
import me.jakubok.nationsmod.gui.miscellaneous.property.PropertyEntry;
import me.jakubok.nationsmod.gui.miscellaneous.property.PropertyListWidget;
import me.jakubok.nationsmod.gui.miscellaneous.property.TextProperty;
import me.jakubok.nationsmod.gui.townScreen.TownScreen;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class ActScreen<T extends LegalOrganisationLawDescription> extends ResizableWindow {
    public ButtonWidget edit, seal, submit, close;
    public final boolean sealed;
    public Act<T> act;
    public LegalOrganisation<T> organisation;
    public PropertyListWidget list;
    List<PropertyEntry> text = new ArrayList<>();
    public ActScreen(Act<T> act, LegalOrganisation<T> organisation, boolean sealed, Screen previousScreen) {
        super(Text.literal(act.getName()).formatted(Formatting.BOLD), 225, 200, 4, previousScreen);
        this.act = act;
        this.organisation = organisation;
        this.sealed = sealed;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void init() {
        this.text.clear();
        super.init();
        assert this.client != null;
        this.text.add(new TextProperty(this.client, Text.literal("Act status"), this.act.status.getDisplayText()));
        this.text.add(new TextProperty(this.client, Text.literal("Affected body"), Text.literal(this.organisation.getName())));
        for (String orderName : this.act.getOrders().keySet()) {
            Order order = this.act.description.getOrders().get(orderName);
            this.text.addAll(order
                    .getOnTriggerMessage()
                    .get(this.act.getOrders().get(orderName), this.organisation, this.client.textRenderer, this.getWindowWidth() - 10)
                    .stream()
                    .map(t -> new TextEntry(this.client, t))
                    .toList()
            );
        }
        for (String ruleName : this.act.existingRules()) {
            List<TextEntry> message = this.act.description.getRulesDescriptions().get(ruleName).getOnChangeMessage.get(
                    this.organisation.law.toString(ruleName),
                    this.act.toString(ruleName),
                    this.client.textRenderer,
                    this.getWindowWidth() - 10
            ).stream().map(t -> new TextEntry(this.client, t)).toList();
            this.text.addAll(message);
        }

        if (this.text.size() < 3) {
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
        ).dimensions(0, 0, 0, 20).build();
        this.submit = ButtonWidget.builder(
                Text.of("Submit"),
                b -> {
                    ClientPlayNetworking.send(Packets.SUBMIT_AN_ACT, PacketByteBufs.create());
                    this.client.setScreen(null);
                }
        ).dimensions(0, 0, 0, 20).build();
        this.seal = ButtonWidget.builder(
                Text.of("Seal"),
                b -> {
                    this.client.setScreen(
                            new AreYouSureScreen(
                                    Text.of("Sealing the act"),
                                    s -> {
                                        ClientPlayNetworking.send(Packets.SEAL_AN_ACT, PacketByteBufs.create());
                                        this.client.setScreen(null);
                                    },
                                    this
                            )
                    );
                }
        ).dimensions(0, 0, 0, 20).build();
        this.close = ButtonWidget.builder(
                Text.of("Close"),
                b -> this.close()
        ).dimensions(0, 0, 0, 20).build();

        if (this.sealed) {
            ResizableWindow.alignButtons(
                    ImmutableList.of(this.submit, this.close),
                    this.getWindowLeft() + 5,
                    this.getWindowBottom() - 25,
                    this.getWindowWidth() - 10
            );
            this.addDrawableChild(this.submit);
            this.addDrawableChild(this.close);
        } else {
            ResizableWindow.alignButtons(
                    ImmutableList.of(this.edit, this.seal, this.close),
                    this.getWindowLeft() + 5,
                    this.getWindowBottom() - 25,
                    this.getWindowWidth() - 10
            );
            this.addDrawableChild(this.edit);
            this.addDrawableChild(this.seal);
            this.addDrawableChild(this.close);
        }

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
