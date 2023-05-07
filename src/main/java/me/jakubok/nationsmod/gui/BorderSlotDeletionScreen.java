package me.jakubok.nationsmod.gui;

import me.jakubok.nationsmod.gui.miscellaneous.SimpleWindow;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class BorderSlotDeletionScreen extends SimpleWindow {

    protected BorderSlotScreen parent;
    protected ButtonWidget yes, no;
    String borderSlotName;

    public BorderSlotDeletionScreen(BorderSlotScreen parent, String borderSlotName, Screen previousScreen) {
        super(Text.of(""), previousScreen);
        this.parent = parent;
        this.borderSlotName = borderSlotName;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);

        drawCenteredTextWithShadow(
            matrices, 
            textRenderer, 
            Text.translatable("gui.nationsmod.delete_a_border_slot_screen.1"), 
            windowCenterHorizontal,
            windowCenterVertical - 20, 
            0xa5081a
        );

        drawCenteredTextWithShadow(
            matrices, 
            textRenderer, 
            Text.translatable("gui.nationsmod.delete_a_border_slot_screen.2"), 
            windowCenterHorizontal,
            windowCenterVertical, 
            0xa5081a
        );
    }

    @Override
    protected void init() {
        super.init();

        this.yes = ButtonWidget.builder(
            Text.translatable("gui.nationsmod.yes"), 
            t -> {
                if (this.parent.isSelected())
                    this.parent.makeUnselected();

                PacketByteBuf buffer = PacketByteBufs.create();
                buffer.writeString(this.borderSlotName);

                ClientPlayNetworking.send(Packets.DELETE_A_BORDER_SLOT, buffer);
                this.client.setScreen(null);
            }
        ).dimensions(
            windowLeft + 7,
            windowBottom - 25, 
            windowCenterHorizontal / 2 - 5, 
            20
        ).build();
        this.addDrawableChild(this.yes);

        this.no = ButtonWidget.builder(
            Text.translatable("gui.nationsmod.no"), 
            t -> this.client.setScreen(parent)
        ).dimensions(
            windowCenterHorizontal,
            windowBottom - 25, 
            windowCenterHorizontal / 2 - 5, 
            20
        ).build();
        this.addDrawableChild(this.no);
    }
    
}
