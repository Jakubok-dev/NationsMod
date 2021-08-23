package me.jakubok.nationsmod.gui;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public class NationCreationScreen extends SimpleWindow {

    TextFieldWidget nationName, provinceName;
    ButtonWidget submit;

    public NationCreationScreen() {
        super(new TranslatableText("gui.nationsmod.nation_creation_screen.title"));
    }
    
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);

        // Nation name:
        drawCenteredText(
            matrices,
            textRenderer, 
            new TranslatableText("gui.nationsmod.nation_creation_screen.nation_name"), 
            windowCenterHorizontal - 75, 
            windowCenterVertical - 20, 
            0xffffff
        );

        // Province name:
        drawCenteredText(
            matrices,
            textRenderer, 
            new TranslatableText("gui.nationsmod.nation_creation_screen.province_name"), 
            windowCenterHorizontal - 75, 
            windowCenterVertical + 5, 
            0xffffff
        );
    }

    @Override
    protected void init() {
        super.init();

        this.nationName = new TextFieldWidget(
            textRenderer,
            windowCenterHorizontal,
            windowCenterVertical - 25,
            100,
            20,
            new TranslatableText("gui.nationsmod.nation_creation_screen.nation_name")
        );
        this.addDrawableChild(this.nationName);

        this.provinceName = new TextFieldWidget(
            textRenderer, 
            windowCenterHorizontal, 
            windowCenterVertical,
            100, 
            20, 
            new TranslatableText("gui.nationsmod.nation_creation_screen.province_name")
        );
        this.addDrawableChild(this.provinceName);

        this.submit = new ButtonWidget(
            windowCenterHorizontal - 64, 
            windowBottom - 25, 
            128, 
            20, 
            new TranslatableText("gui.nationsmod.submit"),
            b -> {}
        );
        this.addDrawableChild(this.submit);
    }
}
