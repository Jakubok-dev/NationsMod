package me.jakubok.nationsmod.gui.polygonsStorageScreen;

import com.mojang.blaze3d.systems.RenderSystem;
import me.jakubok.nationsmod.collection.PolygonAlterationMode;
import me.jakubok.nationsmod.geometry.Polygon;
import me.jakubok.nationsmod.gui.PolygonScreen;
import me.jakubok.nationsmod.gui.miscellaneous.ResizableWindow;
import me.jakubok.nationsmod.networking.ClientNetworking;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PolygonsStorageScreen extends ResizableWindow {

    public final Map<String, Integer> storage;

    protected ButtonWidget addition, deletion, insertion, opening;
    protected PolygonAlterationMode mode;
    protected TextFieldWidget searchBox;
    protected PolygonsListWidget polygonsListWidget;
    protected int selectedSlot;

    public PolygonsStorageScreen(Map<String, Integer> storage, int selectedSlot, PolygonAlterationMode mode, Screen previousScreen) {
        super(Text.of("Polygons storage screen"), previousScreen);
        this.mode = mode;
        this.storage = storage;
        this.selectedSlot = selectedSlot;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        RenderSystem.setShaderTexture(0, new Identifier("textures/gui/social_interactions.png"));
        drawTexture(matrices, this.windowCenterHorizontal() - 73, this.getWindowBottom() - 21, 243, 1, 12, 12);
    }

    @Override
    protected void init() {
        super.init();

        this.polygonsListWidget = new PolygonsListWidget(
                this.client,
                this.storage,
                this.selectedSlot,
                this.getWindowLeft(),
                this.getWindowWidth() - 5,
                this.getWindowHeight() - 52,
                this.getWindowTop() + 25,
                this.getWindowBottom() - 27,
                25,
                this
        );
        this.addDrawableChild(this.polygonsListWidget);

        this.searchBox = new TextFieldWidget(
                textRenderer,
                this.windowCenterHorizontal() - 56,
                this.getWindowBottom() - 25,
                133,
                20,
                Text.of("")
        );
        this.searchBox.setPlaceholder(Text.literal("Search...").formatted(Formatting.ITALIC).formatted(Formatting.GRAY));
        this.searchBox.setChangedListener(polygonsListWidget::onSearchChange);
        this.addDrawableChild(this.searchBox);

        this.addition = ButtonWidget.builder(
                Text.of("ADD"),
                b -> {
                    this.mode = PolygonAlterationMode.ADDITION;
                    this.addition.active = false;
                    this.deletion.active = true;
                    this.insertion.active = true;
                    this.opening.active = true;

                    PacketByteBuf buffer = PacketByteBufs.create();
                    buffer.writeInt(this.mode.ordinal());
                    ClientPlayNetworking.send(Packets.CHANGE_THE_POLYGON_ALTERATION_MODE, buffer);
                }
        ).dimensions(
                this.getWindowLeft(),
                this.getWindowTop() - 25,
                25,
                20
        ).build();
        this.addition.active = this.mode != PolygonAlterationMode.ADDITION;
        this.addDrawableChild(this.addition);

        this.deletion = ButtonWidget.builder(
                Text.of("DEL"),
                b -> {
                    this.mode = PolygonAlterationMode.DELETION;
                    this.addition.active = true;
                    this.deletion.active = false;
                    this.insertion.active = true;
                    this.opening.active = true;

                    PacketByteBuf buffer = PacketByteBufs.create();
                    buffer.writeInt(this.mode.ordinal());
                    ClientPlayNetworking.send(Packets.CHANGE_THE_POLYGON_ALTERATION_MODE, buffer);
                }
        ).dimensions(
                this.getWindowLeft() + (this.windowCenterHorizontal() - this.getWindowLeft()) / 2 + 12,
                this.getWindowTop() - 25,
                25,
                20
        ).build();
        this.deletion.active = this.mode != PolygonAlterationMode.DELETION;
        this.addDrawableChild(this.deletion);

        this.insertion = ButtonWidget.builder(
                Text.of("INS"),
                b -> {
                    this.mode = PolygonAlterationMode.INSERTION;
                    this.addition.active = true;
                    this.deletion.active = true;
                    this.insertion.active = false;
                    this.opening.active = true;

                    PacketByteBuf buffer = PacketByteBufs.create();
                    buffer.writeInt(this.mode.ordinal());
                    ClientPlayNetworking.send(Packets.CHANGE_THE_POLYGON_ALTERATION_MODE, buffer);
                }
        ).dimensions(
                this.windowCenterHorizontal() + (this.getWindowRight() - this.windowCenterHorizontal()) / 2 - 37,
                this.getWindowTop() - 25,
                25,
                20
        ).build();
        this.insertion.active = this.mode != PolygonAlterationMode.INSERTION;
        this.addDrawableChild(this.insertion);

        this.opening = ButtonWidget.builder(
                Text.of("OPN"),
                b -> {
                    this.mode = PolygonAlterationMode.OPENING;
                    this.addition.active = true;
                    this.deletion.active = true;
                    this.insertion.active = true;
                    this.opening.active = false;

                    PacketByteBuf buffer = PacketByteBufs.create();
                    buffer.writeInt(this.mode.ordinal());
                    ClientPlayNetworking.send(Packets.CHANGE_THE_POLYGON_ALTERATION_MODE, buffer);
                }
        ).dimensions(
                this.getWindowRight() - 25,
                this.getWindowTop() - 25,
                25,
                20
        ).build();
        this.opening.active = this.mode != PolygonAlterationMode.OPENING;
        this.addDrawableChild(this.opening);
    }
}
