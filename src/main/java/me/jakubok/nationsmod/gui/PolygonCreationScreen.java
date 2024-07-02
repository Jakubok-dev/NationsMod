package me.jakubok.nationsmod.gui;

import me.jakubok.nationsmod.gui.miscellaneous.ResizableWindow;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class PolygonCreationScreen extends ResizableWindow {

    protected TextFieldWidget nameField;
    protected ButtonWidget submit;

    public PolygonCreationScreen(Screen previousScreen) {
        super(Text.of("Polygon creation"), previousScreen);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredTextWithShadow(
                matrices,
                this.textRenderer,
                Text.of("Name:"),
                this.windowCenterHorizontal() - 75,
                this.windowCenterVertical() - 10,
                0xffffff
        );
    }

    @Override
    protected void init() {
        super.init();
        this.nameField = new TextFieldWidget(
                textRenderer,
                this.windowCenterHorizontal(),
                this.windowCenterVertical() - 15,
                100,
                20,
                Text.of("")
        );
        this.addDrawableChild(this.nameField);

        this.submit = ButtonWidget.builder(
                Text.translatable("gui.nationsmod.submit"),
                t -> {

                    if (this.nameField.getText().trim().equals("") || this.nameField.getText().trim().equals("+"))
                        return;

                    PacketByteBuf buffer = PacketByteBufs.create();
                    buffer.writeString(this.nameField.getText());

                    ClientPlayNetworking.send(Packets.CREATE_A_POLYGON, buffer);

                    t.active = false;
                    this.client.setScreen(null);
                }
        ).dimensions(
                this.windowCenterHorizontal() - 50,
                this.getWindowBottom() - 25,
                100,
                20
        ).build();
        this.addDrawableChild(this.submit);
    }
}
