package me.jakubok.nationsmod.gui.miscellaneous.property;

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

public class PropertyListWidget extends ElementListWidget<PropertyEntry> {

    public PropertyListWidget(MinecraftClient minecraftClient, List<PropertyEntry> properties, int x, int width, int height, int top, int bottom, int itemHeight) {
        super(minecraftClient, width, height, top, bottom, itemHeight);
        this.left = x;
        this.right += x;
        this.setRenderBackground(false);
        this.setRenderHorizontalShadows(false);

        for (PropertyEntry entry : properties) {
            this.addEntry(entry);
        }
    }

    @Override
    protected int getScrollbarPositionX() {
        return this.right - 6;
    }
}
