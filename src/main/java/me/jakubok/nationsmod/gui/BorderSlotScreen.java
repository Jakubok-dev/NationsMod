package me.jakubok.nationsmod.gui;

import me.jakubok.nationsmod.NationsClient;
import me.jakubok.nationsmod.collections.BorderGroup;
import me.jakubok.nationsmod.gui.miscellaneous.SimpleWindow;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class BorderSlotScreen extends SimpleWindow {

    protected BorderGroup slot;
    protected ButtonWidget close, remove, select;
    protected boolean selected;
    protected final int index;

    public BorderSlotScreen(BorderGroup slot, int index, boolean selected, Screen previousScreen) {
        super(Text.of(slot.name), previousScreen);
        this.slot = slot;
        this.selected = selected;
        this.index = index;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void makeSelected() {
        this.selected = true;
        this.select.setMessage(Text.translatable("gui.nationsmod.border_slot_screen.unselect"));
    }

    public void makeUnselected() {
        this.selected = false;
        this.select.setMessage(Text.translatable("gui.nationsmod.border_slot_screen.select"));
        NationsClient.drawer.emptyStorage();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);

        drawCenteredTextWithShadow(
            matrices, 
            textRenderer, 
            Text.of("Blocks:"), 
            windowCenterHorizontal - windowCenterHorizontal / 4,
            (windowCenterVertical - windowTop) / 2 + windowTop,
            0xffffff
        );

        drawCenteredTextWithShadow(
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

        this.select = ButtonWidget.builder(
            Text.translatable("gui.nationsmod.border_slot_screen.select"), 
            t -> {
                if (this.selected) {

                    ClientPlayNetworking.send(Packets.UNSELECT_A_BORDER_SLOT, PacketByteBufs.create());
                    NationsClient.selectedSlot = -1;
                    NationsClient.borderSlot = new BorderGroup();

                    this.makeUnselected();
                    return;
                }

                PacketByteBuf buffer = PacketByteBufs.create();
                buffer.writeString(slot.name);

                NationsClient.borderSlot = new BorderGroup();
                ClientPlayNetworking.send(Packets.SELECT_A_BORDER_SLOT, buffer);
                NationsClient.selectedSlot = this.index;

                this.makeSelected();
            }
        ).dimensions(
            (windowLeft) + 5,
            windowBottom - 25, 
            (windowRight - windowLeft) / 3 - 5,
            20
        ).build();

        if (this.selected)
            this.makeSelected();

        this.addDrawableChild(this.select);

        this.remove = ButtonWidget.builder(
            Text.translatable("gui.nationsmod.border_slot_screen.remove"), 
            t -> this.client.setScreen(new BorderSlotDeletionScreen(this, slot.name, this))
        ).dimensions(
            (windowLeft + (windowRight - windowLeft) / 3) + 1,
            windowBottom - 25,
            (windowRight - windowLeft) / 3 - 2,
            20
        ).build();
        this.addDrawableChild(this.remove);

        this.close = ButtonWidget.builder(
            Text.translatable("gui.nationsmod.border_slot_screen.close"),
            t -> this.close()
        ).dimensions(
            (windowLeft + ((windowRight - windowLeft) / 3)*2) + 2,
            windowBottom - 25,
            (windowRight - windowLeft) / 3 - 5,
            20
        ).build();
        this.addDrawableChild(this.close);
    }
    
}
