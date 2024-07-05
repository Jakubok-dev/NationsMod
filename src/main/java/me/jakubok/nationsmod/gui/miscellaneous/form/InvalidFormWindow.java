package me.jakubok.nationsmod.gui.miscellaneous.form;

import me.jakubok.nationsmod.collection.Pair;
import me.jakubok.nationsmod.gui.miscellaneous.property.PropertyEntry;
import me.jakubok.nationsmod.gui.miscellaneous.property.PropertyListWidget;
import me.jakubok.nationsmod.gui.miscellaneous.ResizableWindow;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;

public class InvalidFormWindow extends ResizableWindow {
    public ButtonWidget comeBack;
    public PropertyListWidget properties;
    public final List<PropertyEntry> errors;
    public InvalidFormWindow(Text title, List<PropertyEntry> errors, int width, int height, int borderRadius, Screen previousScreen) {
        super(title, width, height, borderRadius, previousScreen);
        this.errors = errors;
    }

    @Override
    protected void init() {
        super.init();

        this.properties = new PropertyListWidget(
                this.client,
                this.errors,
                this.getWindowLeft() + 5,
                this.getWindowWidth() - 10,
                this.getWindowHeight() - 52,
                this.getWindowTop() + 25,
                this.getWindowBottom() - 27,
                25
        );
        this.addDrawableChild(this.properties);

        this.comeBack = ButtonWidget.builder(Text.of("Come back"), c -> this.close()).dimensions(
                this.getWindowLeft() + this.getWindowWidth() / 6,
                this.getWindowBottom() - 25,
                2 * this.getWindowWidth() / 3,
                20
        ).build();
        this.addDrawableChild(this.comeBack);
    }
}
