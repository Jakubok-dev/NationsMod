package me.jakubok.nationsmod.gui;

import me.jakubok.nationsmod.gui.miscellaneous.SimpleWindow;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class BorderSlotCreationScreen extends SimpleWindow {

    protected TextFieldWidget nameField;
    protected ButtonWidget submit;

    public BorderSlotCreationScreen() {
        super(new TranslatableText("gui.nationsmod.border_slot_creator_screen.title"));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        
        super.render(matrices, mouseX, mouseY, delta);

        drawCenteredText(
            matrices, 
            this.textRenderer, 
            new TranslatableText("gui.nationsmod.border_slot_creator_screen.name"), 
            windowCenterHorizontal - 75, 
            windowCenterVertical - 10, 
            0xffffff
        );
    }
    
    @Override
    protected void init() {

        this.nameField = new TextFieldWidget(
            textRenderer, 
            windowCenterHorizontal, 
            windowCenterVertical - 15, 
            100, 
            20,
            Text.of("")
        );
        this.addDrawableChild(this.nameField);

        this.submit = new ButtonWidget(
            windowCenterHorizontal - 50,
            windowBottom - 25,
            100,
            20, 
            new TranslatableText("gui.nationsmod.submit"),
            t -> {

                if (this.nameField.getText().trim() == "" || this.nameField.getText().trim() == "+")
                    return;

                PacketByteBuf buffer = PacketByteBufs.create();
                buffer.writeString(this.nameField.getText());

                ClientPlayNetworking.send(Packets.CREATE_A_BORDER_SLOT, buffer);

                t.active = false;
                this.client.setScreen(null);
            }
        );
        this.addDrawableChild(this.submit);

        super.init();
    }
}
