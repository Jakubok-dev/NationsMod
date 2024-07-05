package me.jakubok.nationsmod.gui.miscellaneous.form;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RadioButtonInput extends FormListWidget.FormInputEntry {

    public List<ButtonWidget> buttons = new ArrayList<>();
    int selected = -1;

    public RadioButtonInput(List<Text> labels, Function<Object, Text> validateFunction, MinecraftClient client) {
        super(labels, validateFunction, client);
        for (int i = 1; i < this.labels.size(); i++) {
            this.buttons.add(ButtonWidget.builder(
                    labels.get(i),
                    b -> {
                        if (this.selected >= 0)
                            this.buttons.get(this.selected).active = true;
                        this.selected = this.buttons.indexOf(b);
                        b.active = false;
                    }
            ).dimensions(
                    0,
                    0,
                    this.client.textRenderer.getWidth(labels.get(i)) + 4,
                    20
            ).build());
        }
    }

    @Override
    public Object getInput() {
        return this.selected;
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return this.buttons;
    }

    @Override
    public List<? extends Element> children() {
        return this.buttons;
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
        int margin = 0;
        for (int i = 0; i < this.buttons.size(); i++) {
            ButtonWidget btn = this.buttons.get(i);
            btn.setX(x + entryWidth - entryWidth / 12 - btn.getWidth() - i - margin);
            btn.setY(y);
            margin += btn.getWidth();
            btn.render(matrices, mouseX, mouseY, tickDelta);
        }
    }
}
