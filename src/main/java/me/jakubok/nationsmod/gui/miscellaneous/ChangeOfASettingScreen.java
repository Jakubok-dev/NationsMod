package me.jakubok.nationsmod.gui.miscellaneous;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class ChangeOfASettingScreen extends SimpleWindow {

    public ButtonWidget submit;
    public TextFieldWidget nameField;
    public SubmitFunction submitFunction;

    public ChangeOfASettingScreen(Text title, SubmitFunction submitFunction) {
        super(Text.of("Changing - " + title.asString()));
        this.submitFunction = submitFunction;
    }

    @FunctionalInterface
    public interface SubmitFunction {
        void submit(ChangeOfASettingScreen instance);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(
            matrices,
            textRenderer, 
            Text.of("New value:"), 
            windowCenterHorizontal - 75, 
            windowCenterVertical - 15, 
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
            t -> submitFunction.submit(this)
        );

        this.nameField = new TextFieldWidget(
            textRenderer,
            windowCenterHorizontal,
            windowCenterVertical - 20,
            100,
            20,
            Text.of("New value:")
        );

        this.addDrawableChild(this.submit);
        this.addDrawableChild(this.nameField);
    }
}
