package me.jakubok.nationsmod.gui.miscellaneous;

import me.jakubok.nationsmod.collection.Pair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class PropertyListWidget extends ElementListWidget<PropertyListWidget.PropertyEntry> {

    public PropertyListWidget(MinecraftClient minecraftClient, List<Pair<Text, Text>> properties, int x, int width, int height, int top, int bottom, int itemHeight) {
        super(minecraftClient, width, height, top, bottom, itemHeight);
        this.left = x;
        this.right += x;
        this.setRenderBackground(false);
        this.setRenderHorizontalShadows(false);

        for (Pair<Text, Text> entry : properties) {
            this.addEntry(new PropertyEntry(entry.key, entry.value));
        }
    }

    @Override
    protected int getScrollbarPositionX() {
        return this.right - 6;
    }

    public class PropertyEntry extends ElementListWidget.Entry<PropertyEntry> {

        public Text property, value;

        public PropertyEntry(Text property, Text value) {
            this.property = property;
            this.value = value;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return new ArrayList<>();
        }

        @Override
        public List<? extends Element> children() {
            return new ArrayList<>();
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            TextRenderer textRenderer = PropertyListWidget.this.client.textRenderer;
            int valueWidth = textRenderer.getWidth(value);
            Screen.drawTextWithShadow(
                matrices,
                textRenderer,
                property,
                x,
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
}
