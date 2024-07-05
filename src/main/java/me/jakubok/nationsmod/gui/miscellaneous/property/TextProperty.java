package me.jakubok.nationsmod.gui.miscellaneous.property;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class TextProperty extends PropertyEntry {
    public Text property, value;
    public TextProperty(MinecraftClient client, Text property, Text value) {
        super(client);
        this.property = property;
        this.value = value;
    }

    public List<? extends Selectable> selectableChildren() {
        return new ArrayList<>();
    }

    @Override
    public List<? extends Element> children() {
        return new ArrayList<>();
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        TextRenderer textRenderer = this.client.textRenderer;
        int valueWidth = textRenderer.getWidth(value);
        Screen.drawTextWithShadow(
                matrices,
                textRenderer,
                property,
                x + 2,
                y,
                0xFFFFFF
        );

        Screen.drawTextWithShadow(
                matrices,
                textRenderer,
                value,
                x + entryWidth - valueWidth - 7,
                y,
                0xFFFFFF
        );
    }
}
