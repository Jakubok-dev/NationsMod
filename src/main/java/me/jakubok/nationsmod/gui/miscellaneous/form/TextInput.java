package me.jakubok.nationsmod.gui.miscellaneous.form;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.function.Function;

public class TextInput extends FormListWidget.FormInputEntry {
    public TextFieldWidget textFieldWidget;
    public final MinecraftClient client;
    public TextInput(Text label, MutableText placeholder, Function<Object, Text> validateFunction, MinecraftClient client) {
        super(ImmutableList.of(label, placeholder), validateFunction);
        placeholder.formatted(Formatting.ITALIC).formatted(Formatting.GRAY);
        this.client = client;
        this.textFieldWidget = new TextFieldWidget(this.client.textRenderer, 0, 0, 0, 20, Text.of(""));
        this.textFieldWidget.setPlaceholder(placeholder);
    }

    @Override
    public Object getInput() {
        return this.textFieldWidget.getText();
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return ImmutableList.of(this.textFieldWidget);
    }

    @Override
    public List<? extends Element> children() {
        return ImmutableList.of(this.textFieldWidget);
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        this.client.textRenderer.drawWithShadow(
                matrices,
                this.labels.get(0),
                x + (float) entryWidth / 12,
                y + (float) (entryHeight - this.client.textRenderer.fontHeight) / 2,
                0xffffff
        );
        this.textFieldWidget.setWidth(2 * entryWidth / 5);
        this.textFieldWidget.setY(y);
        this.textFieldWidget.setX(x + (11 * entryWidth / 12) - this.textFieldWidget.getWidth());
        this.textFieldWidget.render(matrices, mouseX, mouseY, tickDelta);
    }
}
