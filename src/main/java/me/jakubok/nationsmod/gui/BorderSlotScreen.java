package me.jakubok.nationsmod.gui;

import me.jakubok.nationsmod.collections.BorderGroup;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class BorderSlotScreen extends SimpleWindow {

    protected BorderGroup slot;
    protected ButtonWidget close, remove, select;

    public BorderSlotScreen(BorderGroup slot) {
        super(Text.of(slot.name));
        this.slot = slot;
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
            t -> {}
        );
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
