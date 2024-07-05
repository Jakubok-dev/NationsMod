package me.jakubok.nationsmod.gui.miscellaneous.property;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ElementListWidget;

public abstract class PropertyEntry extends ElementListWidget.Entry<PropertyEntry> {

    public final MinecraftClient client;

    public PropertyEntry(MinecraftClient client) {
        this.client = client;
    }
}
