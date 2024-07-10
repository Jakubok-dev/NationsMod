package me.jakubok.nationsmod.gui.miscellaneous.property;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ElementListWidget;

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
