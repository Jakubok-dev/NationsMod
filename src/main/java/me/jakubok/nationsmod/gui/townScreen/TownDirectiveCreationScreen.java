package me.jakubok.nationsmod.gui.townScreen;

import me.jakubok.nationsmod.administration.Directive;
import me.jakubok.nationsmod.administration.Town;
import me.jakubok.nationsmod.gui.miscellaneous.SimpleWindow;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class TownDirectiveCreationScreen extends SimpleWindow {

    public final Directive<Town> directive;
    public ButtonWidget submit;
    public TextFieldWidget nameField;

    public TownDirectiveCreationScreen(Directive<Town> draft, Screen previousScreen) {
        super(Text.of("Directive creation"), previousScreen);
        this.directive = draft;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(
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

        this.submit = new ButtonWidget(
            windowCenterHorizontal - 64, 
            windowBottom - 25, 
            128, 
            20, 
            new TranslatableText("gui.nationsmod.submit"),
            t -> {}
        );

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
