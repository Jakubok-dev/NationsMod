package me.jakubok.nationsmod.gui;

import me.jakubok.nationsmod.NationsClient;
import me.jakubok.nationsmod.collections.BorderGroup;
import me.jakubok.nationsmod.gui.miscellaneous.SimpleWindow;
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

    public boolean isSelected() {
        return this.selected;
    }

    public void makeSelected() {
        this.selected = true;
        this.select.setMessage(new TranslatableText("gui.nationsmod.border_slot_screen.unselect"));
    }

    public void makeUnselected() {
        this.selected = false;
        this.select.setMessage(new TranslatableText("gui.nationsmod.border_slot_screen.select"));
        NationsClient.drawer.emptyStorage();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);

        drawCenteredText(
            matrices, 
            textRenderer, 
            Text.of("Blocks:"), 
            windowCenterHorizontal - windowCenterHorizontal / 4,
            (windowCenterVertical - windowTop) / 2 + windowTop,
            0xffffff
        );

        drawCenteredText(
            matrices, 
            textRenderer, 
            Text.of(this.slot.getBorderSize() + ""), 
            windowCenterHorizontal + windowCenterHorizontal / 4,
            (windowCenterVertical - windowTop) / 2 + windowTop,
            0xffffff
        );
    }

    @Override
    protected void init() {
        super.init();

        this.select = new ButtonWidget(
            (windowLeft) + 5,
            windowBottom - 25, 
            (windowRight - windowLeft) / 3 - 5,
            20,
            new TranslatableText("gui.nationsmod.border_slot_screen.select"), 
            t -> {
                if (this.selected) {

                    ClientPlayNetworking.send(Packets.UNSELECT_A_BORDER_SLOT_PACKET, PacketByteBufs.create());

                    this.makeUnselected();
                    return;
                }

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
            (windowLeft + (windowRight - windowLeft) / 3) + 1,
            windowBottom - 25,
            (windowRight - windowLeft) / 3 - 2,
            20,
            new TranslatableText("gui.nationsmod.border_slot_screen.remove"), 
            t -> this.client.setScreen(new BorderSlotDeletionScreen(this, slot.name))
        );
        this.addDrawableChild(this.remove);

        this.close = new ButtonWidget(
            (windowLeft + ((windowRight - windowLeft) / 3)*2) + 2,
            windowBottom - 25,
            (windowRight - windowLeft) / 3 - 5,
            20,
            new TranslatableText("gui.nationsmod.border_slot_screen.close"),
            t -> this.client.setScreen(null)
        );
        this.addDrawableChild(this.close);
    }
    
}
