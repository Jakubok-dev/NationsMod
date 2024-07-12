package me.jakubok.nationsmod.gui.miscellaneous;

import com.google.common.collect.ImmutableList;
import me.jakubok.nationsmod.administration.law.Petition;
import me.jakubok.nationsmod.gui.miscellaneous.property.PropertyEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;

public class PetitionEntry extends PropertyEntry {
    public ButtonWidget nameButton;
    public PetitionEntry(MinecraftClient client, Petition<?> petition) {
        super(client);
        this.nameButton = ButtonWidget.builder(
                Text.of(petition.act.getName()),
                b -> {}
        ).dimensions(0, 0, 0, 20).build();
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return ImmutableList.of(nameButton);
    }

    @Override
    public List<? extends Element> children() {
        return ImmutableList.of(nameButton);
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        this.nameButton.setMessage(Text.literal(this.client.textRenderer.trimToWidth(this.nameButton.getMessage().getString(), entryWidth / 2)));
        this.nameButton.setX(x);
        this.nameButton.setY(y);
        this.nameButton.setWidth(this.client.textRenderer.getWidth(this.nameButton.getMessage()) + 8);
        this.nameButton.render(matrices, mouseX, mouseY, tickDelta);

        this.client.textRenderer.drawWithShadow(
                matrices,
                Text.of("Petition"),
                x + entryWidth - this.client.textRenderer.getWidth(Text.of("Petition")),
                y + (float) (20 - this.client.textRenderer.fontHeight) / 2,
                0xFFFFFF
        );
    }
}
