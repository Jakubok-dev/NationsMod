package me.jakubok.nationsmod.gui;

import me.jakubok.nationsmod.gui.miscellaneous.ResizableWindow;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class PolygonDeletionScreen extends ResizableWindow {
    public final int polygonIndex;
    protected ButtonWidget yes, no;
    public PolygonDeletionScreen(int polygonIndex, Screen previousScreen) {
        super(Text.of("Polygon deletion"), previousScreen);
        this.polygonIndex = polygonIndex;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);

        drawCenteredTextWithShadow(
                matrices,
                textRenderer,
                Text.translatable("gui.nationsmod.delete_a_border_slot_screen.1"),
                this.windowCenterHorizontal(),
                this.windowCenterVertical() - 20,
                0xa5081a
        );

        drawCenteredTextWithShadow(
                matrices,
                textRenderer,
                Text.translatable("gui.nationsmod.delete_a_border_slot_screen.2"),
                this.windowCenterHorizontal(),
                this.windowCenterVertical(),
                0xa5081a
        );
    }

    @Override
    protected void init() {
        super.init();

        this.yes = ButtonWidget.builder(
                Text.translatable("gui.nationsmod.yes"),
                t -> {
                    PacketByteBuf buffer = PacketByteBufs.create();
                    buffer.writeInt(polygonIndex);

                    ClientPlayNetworking.send(Packets.REMOVE_A_POLYGON, buffer);
                    this.client.setScreen(null);
                }
        ).dimensions(
                this.getWindowLeft() + 7,
                this.getWindowBottom() - 25,
                this.windowCenterHorizontal() / 2 - 5,
                20
        ).build();
        this.addDrawableChild(this.yes);

        this.no = ButtonWidget.builder(
                Text.translatable("gui.nationsmod.no"),
                t -> this.close()
        ).dimensions(
                this.windowCenterHorizontal(),
                this.getWindowBottom() - 25,
                this.windowCenterHorizontal() / 2 - 5,
                20
        ).build();
        this.addDrawableChild(this.no);
    }
}
