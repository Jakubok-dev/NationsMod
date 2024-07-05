package me.jakubok.nationsmod.gui.miscellaneous.property;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

@FunctionalInterface
public interface EditScreenFactory<T> {

    Screen get(MutableProperty<T> mutableProperty, MinecraftClient client);
}
