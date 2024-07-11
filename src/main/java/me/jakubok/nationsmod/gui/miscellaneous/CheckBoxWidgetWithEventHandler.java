package me.jakubok.nationsmod.gui.miscellaneous;

import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class CheckBoxWidgetWithEventHandler extends CheckboxWidget {
    public final Consumer<CheckBoxWidgetWithEventHandler> eventHandler;
    public CheckBoxWidgetWithEventHandler(int x, int y, int width, int height, Text message, boolean checked, Consumer<CheckBoxWidgetWithEventHandler> onPress) {
        super(x, y, width, height, message, checked);
        this.eventHandler = onPress;
        if (this.eventHandler == null)
            this.active = false;
    }

    public CheckBoxWidgetWithEventHandler(int x, int y, int width, int height, Text message, boolean checked, boolean showMessage, Consumer<CheckBoxWidgetWithEventHandler> onPress) {
        super(x, y, width, height, message, checked, showMessage);
        this.eventHandler = onPress;
        if (this.eventHandler == null)
            this.active = false;
    }

    @Override
    public void onPress() {
        super.onPress();
        this.eventHandler.accept(this);
    }
}
