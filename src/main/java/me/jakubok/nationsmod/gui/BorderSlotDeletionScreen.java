package me.jakubok.nationsmod.gui;

import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class BorderSlotDeletionScreen extends SimpleWindow {

    protected BorderSlotScreen parent;
    protected ButtonWidget yes, no;
    String borderSlotName;

    public BorderSlotDeletionScreen(BorderSlotScreen parent, String borderSlotName) {
        super(Text.of(""));
        this.parent = parent;
        this.borderSlotName = borderSlotName;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);

        drawCenteredText(
            matrices, 
            textRenderer, 
            new TranslatableText("gui.nationsmod.delete_a_border_slot_screen.1"), 
            this.windowCenterHorizontal,
            this.windowCenterVertical - 20, 
            0xa5081a
        );

        drawCenteredText(
            matrices, 
            textRenderer, 
            new TranslatableText("gui.nationsmod.delete_a_border_slot_screen.2"), 
            this.windowCenterHorizontal,
            this.windowCenterVertical, 
            0xa5081a
        );
    }

    @Override
    protected void init() {
        super.init();

        this.yes = new ButtonWidget(
            this.windowLeft + 7,
            this.windowBottom - 25, 
            this.windowCenterHorizontal / 2 - 5, 
            20,
            new TranslatableText("gui.nationsmod.yes"), 
            t -> {
                if (this.parent.isSelected())
                    this.parent.makeUnselected();

                PacketByteBuf buffer = PacketByteBufs.create();
                buffer.writeString(this.borderSlotName);

                ClientPlayNetworking.send(Packets.DELETE_A_BORDER_SLOT_PACKET, buffer);
                this.client.setScreen(null);
            }
        );
        this.addDrawableChild(this.yes);

        this.no = new ButtonWidget(
            this.windowCenterHorizontal,
            this.windowBottom - 25, 
            this.windowCenterHorizontal / 2 - 5, 
            20,
            new TranslatableText("gui.nationsmod.no"), 
            t -> this.client.setScreen(parent)
        );
        this.addDrawableChild(this.no);
    }
    
}
