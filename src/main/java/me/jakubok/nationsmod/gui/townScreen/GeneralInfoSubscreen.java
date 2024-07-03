package me.jakubok.nationsmod.gui.townScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.jakubok.nationsmod.administration.nation.Nation;
import me.jakubok.nationsmod.administration.province.Province;
import me.jakubok.nationsmod.collection.Pair;
import me.jakubok.nationsmod.gui.miscellaneous.*;
import me.jakubok.nationsmod.networking.ClientNetworking;
import me.jakubok.nationsmod.networking.Packets;
import me.jakubok.nationsmod.registries.ItemRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class GeneralInfoSubscreen {
    public final Subscreen<TabWindow> subscreen;

    public final List<Pair<Text, Text>> propertyEntries;
    public PropertyListWidget list;

    public GeneralInfoSubscreen(TownScreen inst) {
        this.subscreen = new Subscreen<>(Text.of("General info"), new ItemStack(ItemRegistry.TOWN_INDEPENDENCE_DECLARATION), this::render, this::init);

        this.propertyEntries = Arrays.asList(
                new Pair<>(Text.of("Name:"), Text.of(inst.town.getName())),
                new Pair<>(Text.of("Government:"), inst.town.formOfGovernment.getDisplayName()),
                new Pair<>(Text.of("Citizens:"), Text.of(inst.town.getAIMembers().size() + inst.town.getPlayerMembers().size() + "")),
                new Pair<>(Text.of("Districts:"), Text.of(inst.town.getTheListOfDistrictsIDs().size() + "")),
                new Pair<>(Text.of("Province:"), Text.of("-")),
                new Pair<>(Text.of("Nation:"), Text.of("-")),
                new Pair<>(Text.of("Petition support:"), Text.of(inst.town.getThePetitionSupport() + "%")),
                new Pair<>(Text.of("Citizenship:"), inst.town.getTheCitizenshipApprovement().displayText)
        );
    }

    protected void render(MatrixStack matrices, int mouseX, int mouseY, float delta, TabWindow instance) {}

    protected void init(TabWindow instance) {
        this.list = new PropertyListWidget(
                instance.getClient(),
                this.propertyEntries,
                instance.getWindowLeft(),
                instance.getWindowWidth() - 5,
                instance.getWindowHeight() - 30,
                instance.getWindowTop() + 25,
                instance.getWindowBottom() - 5,
                20
        );
        instance.addDrawableChild(this.list);
    }
}
