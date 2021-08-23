package me.jakubok.nationsmod.gui;

import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public class MainScreen extends Screen {

    protected final int windowLeft = 0;
    protected final int windowRight = windowLeft + 480;
    
    protected final int windowTop = 0;
    protected final int windowBottom = windowTop + 255;

    protected final int windowCenterHorizontal = (windowLeft + windowRight) / 2;
    protected final int windowCenterVertical = (windowTop + windowBottom) / 2;

    public MainScreen() {
        super(new TranslatableText("gui.nationsmod.main_screen.title"));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(
            matrices, 
            this.textRenderer, 
            this.title, 
            windowCenterHorizontal, 
            windowCenterVertical - 50,
            0xffffff
        );
    }

    @Override
    protected void init() {
        super.init();

        // windowCenterHorizontal - 100, 
        // windowCenterVertical - 12,

        this.addDrawableChild(new ButtonWidget(
            this.windowCenterHorizontal - 100, 
            windowCenterVertical - 12, 
            200, 
            20, 
            new TranslatableText("gui.nationsmod.main_screen.towns_button"), 
            b ->
                 ClientPlayNetworking.send(Packets.PREPARE_TOWNS_SCREEN_PACKET, PacketByteBufs.create())
        ));

        this.addDrawableChild(new ButtonWidget(
            windowCenterHorizontal - 100, 
            windowCenterVertical + 12, 
            200, 
            20, 
            new TranslatableText("gui.nationsmod.main_screen.nations_button"), 
            b -> {}
        ));
    }
}
