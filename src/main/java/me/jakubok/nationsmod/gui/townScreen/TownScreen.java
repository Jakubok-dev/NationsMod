package me.jakubok.nationsmod.gui.townScreen;

import java.util.ArrayList;
import java.util.List;

import me.jakubok.nationsmod.administration.law.Act;
import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.administration.town.TownLawDescription;
import me.jakubok.nationsmod.gui.miscellaneous.Subscreen;
import me.jakubok.nationsmod.gui.miscellaneous.TabWindow;
import me.jakubok.nationsmod.networking.Packets;
import me.jakubok.nationsmod.registries.ItemRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class TownScreen extends TabWindow {

    protected final Town town;
    public final GeneralInfoSubscreen generalInfo;
    public final PetitionsAndDirectivesSubscreen petitionsAndDirectives;
    public final Act<TownLawDescription> act;
    public ButtonWidget submitPetitionButton;

    public TownScreen(Town town, Screen previousScreen, Act<TownLawDescription> act) {
        super(Text.of(town.getName()), 250, 170, 4, previousScreen);
        this.town = town;
        this.act = act;
        this.generalInfo = new GeneralInfoSubscreen(this);
        this.petitionsAndDirectives = new PetitionsAndDirectivesSubscreen(this);
    }

    @Override
    protected List<Subscreen<TabWindow>> getTabs() {
        
        List<Subscreen<TabWindow>> tabs = new ArrayList<>();
        tabs.add(this.generalInfo.subscreen);

        tabs.add(new Subscreen<>(
            Text.of("Districts"),
            new ItemStack(ItemRegistry.DISTRICT_DECLARATION), 
            (MatrixStack matrices, int mouseX, int mouseY, float delta, TabWindow instance) -> {
            },
            null
        ));

        tabs.add(new Subscreen<>(
            Text.of("Citizens"),
            new ItemStack(Items.PLAYER_HEAD), 
            (MatrixStack matrices, int mouseX, int mouseY, float delta, TabWindow instance) -> {
            },
            null
        ));

        tabs.add(this.petitionsAndDirectives.subscreen);
        
        return tabs;
    }

    @Override
    protected void init() {
        super.init();
        this.submitPetitionButton = ButtonWidget.builder(Text.of("Write onto the act"), c -> {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeNbt(this.act.writeToNbtAndReturn(new NbtCompound()));
            ClientPlayNetworking.send(Packets.SYNCHRONISE_AN_ACT, buf);
            assert this.client != null;
            this.client.setScreen(null);
        }).dimensions(
                this.getWindowLeft() + 5,
                this.getWindowBottom() - 25,
                this.getWindowWidth() - 10,
                20
        ).build();
        if (this.act != null)
            this.addDrawableChild(this.submitPetitionButton);
    }
}
