package me.jakubok.nationsmod.gui.miscellaneous;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;

public class RadioButtonListWidget extends ElementListWidget<RadioButtonListWidget.RadioButtonEntry> {

    int selected = -1;

    public RadioButtonListWidget(MinecraftClient minecraftClient, List<Text> properties, int x, int width, int height, int top, int bottom, int itemHeight) {
        super(minecraftClient, width, height, top, bottom, itemHeight);
        this.left = x;
        this.right += x;
        this.setRenderBackground(false);
        this.setRenderHorizontalShadows(false);

        for (int i = 0; i < properties.size(); i++)
            this.addEntry(new RadioButtonEntry(i, this, properties.get(i)));
    }

    @Override
    protected int getScrollbarPositionX() {
        return this.right - 6;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        if (this.selected != -1)
            this.getEntry(this.selected).btn.active = true;
        this.selected = selected;
        this.getEntry(this.selected).btn.active = false;
    }

    public static class RadioButtonEntry extends ElementListWidget.Entry<RadioButtonEntry> {
        public final ButtonWidget btn;
        public final RadioButtonListWidget widget;
        public final int index;

        public RadioButtonEntry(int index, RadioButtonListWidget widget, Text display) {
            this.widget = widget;
            this.index = index;
            this.btn = ButtonWidget.builder(
                    display,
                    b -> this.widget.setSelected(this.index)
            ).dimensions(
                    0,
                    0,
                    2 * widget.width / 3,
                    20
            ).build();
        }


        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(this.btn);
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(this.btn);
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.btn.setX(x + entryWidth / 6);
            this.btn.setY(y + (entryHeight - this.btn.getHeight()) / 2);
            this.btn.setWidth(2 * entryWidth / 3 + 7);
            this.btn.render(matrices, mouseX, mouseY, tickDelta);
        }
    }
}
