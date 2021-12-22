package me.jakubok.nationsmod.gui.miscellaneous;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class Subscreen<T> {
    public final Text name;
    public final ItemStack icon;
    public final RenderFunction<T> render;
    public final InitFunction<T> init;
    public final RemoveFunction<T> remove;

    public Subscreen(Text name, ItemStack icon, RenderFunction<T> render, InitFunction<T> init, RemoveFunction<T> remove) {
        this.name = name;
        this.icon = icon;
        this.render = render;
        this.init = init;
        this.remove = remove;
    }

    @FunctionalInterface
    public interface RenderFunction<T> {
        void render(MatrixStack matrices, int mouseX, int mouseY, float delta, T instance);
    }

    @FunctionalInterface
    public interface InitFunction<T> {
        void init(T instance);
    }

    @FunctionalInterface
    public interface RemoveFunction<T> {
        void remove(T instance);
    }
}
