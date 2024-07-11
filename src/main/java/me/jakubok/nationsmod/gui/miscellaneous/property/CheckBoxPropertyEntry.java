package me.jakubok.nationsmod.gui.miscellaneous.property;

import com.google.common.collect.ImmutableList;
import me.jakubok.nationsmod.gui.miscellaneous.CheckBoxWidgetWithEventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;

public class CheckBoxPropertyEntry extends PropertyEntry {
    public CheckBoxWidgetWithEventHandler checkboxWidget;

    public CheckBoxPropertyEntry(MinecraftClient client, Text message, boolean checked, Consumer<CheckBoxWidgetWithEventHandler> onPress) {
        super(client);
        this.checkboxWidget = new CheckBoxWidgetWithEventHandler(0, 0, 0, 20, message, checked, onPress);
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return ImmutableList.of(this.checkboxWidget);
    }

    @Override
    public List<? extends Element> children() {
        return ImmutableList.of(this.checkboxWidget);
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        this.checkboxWidget.setY(y + (entryHeight - this.checkboxWidget.getHeight()) / 2);
        this.checkboxWidget.setX(x);
        this.checkboxWidget.setWidth(entryWidth);
        this.checkboxWidget.render(matrices, mouseX, mouseY, tickDelta);
    }
}
