package me.jakubok.nationsmod.gui.townScreen;

import me.jakubok.nationsmod.gui.miscellaneous.SimpleWindow;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TownDirectiveCreationScreen extends SimpleWindow {

    public ButtonWidget submit;
    public TextFieldWidget nameField;

    public TownDirectiveCreationScreen(Screen previousScreen) {
        super(Text.of("Directive creation"), previousScreen);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredTextWithShadow(
            matrices,
            textRenderer, 
            Text.of("Directive name:"), 
            windowCenterHorizontal - 75, 
            windowTop + 30, 
            0xffffff
        );
    }
    
    @Override
    protected void init() {
        super.init();

        this.submit = ButtonWidget.builder(
            Text.translatable("gui.nationsmod.submit"),
            t -> {}
        ).dimensions(
            windowCenterHorizontal - 64, 
            windowBottom - 25, 
            128, 
            20
        ).build();

        this.nameField = new TextFieldWidget(
            textRenderer,
            windowCenterHorizontal,
            windowTop + 25,
            100,
            20,
            Text.of("Directive name:")
        );

        this.addDrawableChild(this.submit);
        this.addDrawableChild(this.nameField);
    }
}
