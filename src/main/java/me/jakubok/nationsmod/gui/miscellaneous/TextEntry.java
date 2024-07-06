package me.jakubok.nationsmod.gui.miscellaneous;

import com.google.common.collect.ImmutableList;
import me.jakubok.nationsmod.gui.miscellaneous.property.PropertyEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;

import java.util.List;

public class TextEntry extends PropertyEntry {

    public OrderedText text;
    public TextEntry(MinecraftClient client, OrderedText text) {
        super(client);
        this.text = text;
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return ImmutableList.of();
    }

    @Override
    public List<? extends Element> children() {
        return ImmutableList.of();
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        this.client.textRenderer.drawWithShadow(
                matrices,
                this.text,
                x + (float) (entryWidth - this.client.textRenderer.getWidth(this.text)) / 2,
                y + (float) (entryHeight - this.client.textRenderer.fontHeight) / 2,
                0xffffff
        );
    }
}
