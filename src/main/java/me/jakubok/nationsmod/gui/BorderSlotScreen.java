package me.jakubok.nationsmod.gui;

import me.jakubok.nationsmod.collections.BorderGroup;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class BorderSlotScreen extends SimpleWindow {

    protected BorderGroup slot;
    protected ButtonWidget close, remove, select;
    protected boolean selected;

    public BorderSlotScreen(BorderGroup slot, boolean selected) {
        super(Text.of(slot.name));
        this.slot = slot;
        this.selected = selected;
    }

    private void makeSelected() {
        this.select.active = false;
        this.select.setMessage(new TranslatableText("gui.nationsmod.border_slot_screen.selected"));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);

        drawCenteredText(
            matrices, 
            textRenderer, 
            Text.of("Blocks:"), 
            this.windowCenterHorizontal - this.windowCenterHorizontal / 4,
            (this.windowCenterVertical - this.windowTop) / 2 + this.windowTop,
            0xffffff
        );

        drawCenteredText(
            matrices, 
            textRenderer, 
            Text.of(this.slot.getBorderSize() + ""), 
            this.windowCenterHorizontal + this.windowCenterHorizontal / 4,
            (this.windowCenterVertical - this.windowTop) / 2 + this.windowTop,
            0xffffff
        );
    }

    @Override
    protected void init() {
        super.init();

        this.select = new ButtonWidget(
            (this.windowLeft) + 5,
            this.windowBottom - 25, 
            (this.windowRight - this.windowLeft) / 3 - 5,
            20,
            new TranslatableText("gui.nationsmod.border_slot_screen.select"), 
            t -> {
                PacketByteBuf buffer = PacketByteBufs.create();
                buffer.writeString(slot.name);

                ClientPlayNetworking.send(Packets.SELECT_A_BORDER_SLOT_PACKET, buffer);

                this.makeSelected();
            }
        );

        if (this.selected)
            this.makeSelected();

        this.addDrawableChild(this.select);

        this.remove = new ButtonWidget(
            (this.windowLeft + (this.windowRight - this.windowLeft) / 3) + 1,
            this.windowBottom - 25,
            (this.windowRight - this.windowLeft) / 3 - 2,
            20,
            new TranslatableText("gui.nationsmod.border_slot_screen.remove"), 
            t -> this.client.setScreen(new BorderSlotDeletionScreen(this, slot.name))
        );
        this.addDrawableChild(this.remove);

        this.close = new ButtonWidget(
            (this.windowLeft + ((this.windowRight - this.windowLeft) / 3)*2) + 2,
            this.windowBottom - 25,
            (this.windowRight - this.windowLeft) / 3 - 5,
            20,
            new TranslatableText("gui.nationsmod.border_slot_screen.close"),
            t -> this.client.setScreen(null)
        );
        this.addDrawableChild(this.close);
    }
    
}
