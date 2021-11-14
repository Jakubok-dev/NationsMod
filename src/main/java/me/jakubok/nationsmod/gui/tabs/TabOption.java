package me.jakubok.nationsmod.gui.tabs;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class TabOption {
    public final Text name;
    public final ItemStack icon;
    public final RenderFunction render;
    public final InitFunction init;
    public final RemoveFunction remove;

    public TabOption(Text name, ItemStack icon, RenderFunction render, InitFunction init, RemoveFunction remove) {
        this.name = name;
        this.icon = icon;
        this.render = render;
        this.init = init;
        this.remove = remove;
    }

    @FunctionalInterface
    public interface RenderFunction {
        void render(MatrixStack matrices, int mouseX, int mouseY, float delta);
    }

    @FunctionalInterface
    public interface InitFunction {
        void init();
    }

    @FunctionalInterface
    public interface RemoveFunction {
        void remove();
    }
}
