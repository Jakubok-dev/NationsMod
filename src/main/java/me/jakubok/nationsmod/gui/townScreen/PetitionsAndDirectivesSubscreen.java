package me.jakubok.nationsmod.gui.townScreen;

import me.jakubok.nationsmod.gui.miscellaneous.SimpleWindow;
import me.jakubok.nationsmod.gui.miscellaneous.Subscreen;
import me.jakubok.nationsmod.gui.miscellaneous.TabWindow;
import me.jakubok.nationsmod.registries.ItemRegistry;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class PetitionsAndDirectivesSubscreen {

    public final Subscreen<TabWindow> subscreen;
    int page = 0;
    public final ButtonWidget up, down;

    public PetitionsAndDirectivesSubscreen(TownScreen inst) {
        this.subscreen = new Subscreen<>(
            Text.of("Petitions & directives"), 
            new ItemStack(ItemRegistry.PARCHMENT), 
            (MatrixStack matrices, int mouseX, int mouseY, float delta, TabWindow instance) -> this.render(matrices, mouseX, mouseY, delta, instance), 
            instance -> this.init(instance) 
        );

        this.up = ButtonWidget.builder(
            Text.of("▲"), 
            t -> {
                page--;
                inst.reload();
            }
        ).dimensions(
            SimpleWindow.windowLeft + 5, 
            SimpleWindow.windowTop + 5, 
            20, 
            20
        ).build();

        this.down = ButtonWidget.builder(
            Text.of("▼"), 
            t -> {
                page++;
                inst.reload();
            }
        ).dimensions(
            SimpleWindow.windowLeft + 5, 
            SimpleWindow.windowBottom - 25, 
            20, 
            20
        ).build();
    }

    protected void render(MatrixStack matrices, int mouseX, int mouseY, float delta, TabWindow instance) {
        
    }

    protected void init(TabWindow instance) {
        instance.addDrawableChild(this.up);
        instance.addDrawableChild(this.down);
    }
}
