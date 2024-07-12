package me.jakubok.nationsmod.gui.act;

import com.google.common.collect.ImmutableList;
import me.jakubok.nationsmod.administration.abstractEntities.LegalOrganisation;
import me.jakubok.nationsmod.administration.abstractEntities.LegalOrganisationLawDescription;
import me.jakubok.nationsmod.administration.law.Order;
import me.jakubok.nationsmod.administration.law.Petition;
import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.gui.miscellaneous.ResizableWindow;
import me.jakubok.nationsmod.gui.miscellaneous.TextEntry;
import me.jakubok.nationsmod.gui.miscellaneous.property.PropertyEntry;
import me.jakubok.nationsmod.gui.miscellaneous.property.PropertyListWidget;
import me.jakubok.nationsmod.gui.miscellaneous.property.TextProperty;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class PetitionScreen<T extends LegalOrganisationLawDescription> extends ResizableWindow {
    public final Petition<T> petition;
    public final LegalOrganisation<T> organisation;
    public boolean signable;
    public ButtonWidget sign, closeButton;
    public PropertyListWidget list;
    List<PropertyEntry> text = new ArrayList<>();

    public PetitionScreen(Petition<T> petition, LegalOrganisation<T> organisation, boolean signable, Screen previousScreen) {
        super(Text.literal(petition.act.getName()).formatted(Formatting.BOLD), 225, 200, 4, previousScreen);
        this.petition = petition;
        this.organisation = organisation;
        this.signable = signable;
    }

    @Override
    protected void init() {
        this.text.clear();
        super.init();

        assert this.client != null;
        assert this.client.player != null;
        this.text.add(new TextProperty(this.client, Text.literal("Act status"), this.petition.act.status.getDisplayText()));
        this.text.add(new TextProperty(this.client, Text.literal("Affected body"), Text.literal(this.organisation.getName())));
        for (String orderName : this.petition.act.getOrders().keySet()) {
            Order order = this.petition.act.description.getOrders().get(orderName);
            this.text.addAll(order
                    .getOnTriggerMessage()
                    .get(this.petition.act.getOrders().get(orderName), this.organisation, this.client.textRenderer, this.getWindowWidth() - 10)
                    .stream()
                    .map(t -> new TextEntry(this.client, t))
                    .toList()
            );
        }
        for (String ruleName : this.petition.act.existingRules()) {
            List<TextEntry> message = this.petition.act.description.getRulesDescriptions().get(ruleName).getOnChangeMessage.get(
                    this.organisation.law.toString(ruleName),
                    this.petition.act.toString(ruleName),
                    this.client.textRenderer,
                    this.getWindowWidth() - 10
            ).stream().map(t -> new TextEntry(this.client, t)).toList();
            this.text.addAll(message);
        }
        this.text.add(new TextEntry(this.client, Text.literal("Signed by").formatted(Formatting.BOLD).asOrderedText()));
        for (PlayerAccount account : this.petition.playerSignees) {
            this.text.add(new TextEntry(this.client, Text.literal(account.name).asOrderedText()));
            if (account.equals(new PlayerAccount(this.client.player)))
                this.signable = false;
        }
        if (this.petition.playerSignees.size() < 1)
            this.text.add(new TextEntry(this.client, Text.literal("Nobody").asOrderedText()));

        this.windowHeight = Math.min(this.windowHeight, 65 + 15 * this.text.size());

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


        this.sign = ButtonWidget.builder(
                Text.of("Sign"),
                b -> {
                    NbtCompound nbt = new NbtCompound();
                    nbt.putUuid("unit", this.organisation.getId());
                    nbt.putUuid("petition", this.petition.act.getID());
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeNbt(nbt);
                    ClientPlayNetworking.send(Packets.SIGN_A_PETITION, buf);
                    this.client.setScreen(null);
                }
        ).dimensions(0, 0, 0, 20).build();
        this.closeButton = ButtonWidget.builder(
                Text.of("Close"),
                b -> this.close()
        ).dimensions(0, 0, 0, 20).build();
        if (this.signable) {
            alignButtons(
                    ImmutableList.of(this.sign, this.closeButton),
                    this.getWindowLeft() + 5,
                    this.getWindowBottom() - 25,
                    this.getWindowWidth() - 10
            );
            this.addDrawableChild(this.sign);
        } else {
            alignButtons(
                    ImmutableList.of(this.closeButton),
                    this.getWindowLeft() + 5,
                    this.getWindowBottom() - 25,
                    this.getWindowWidth() - 10
            );
        }
        this.addDrawableChild(this.closeButton);
    }
}
