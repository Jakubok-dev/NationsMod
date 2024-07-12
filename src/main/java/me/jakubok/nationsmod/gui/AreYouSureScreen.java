package me.jakubok.nationsmod.gui;

import com.google.common.collect.ImmutableList;
import me.jakubok.nationsmod.gui.miscellaneous.ResizableWindow;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class AreYouSureScreen extends ResizableWindow {
    public ButtonWidget yes, no;
    public final Consumer<AreYouSureScreen> onConfirm;
    public AreYouSureScreen(Text title, Consumer<AreYouSureScreen> onConfirm, Screen previousScreen) {
        super(title, 200, 100, 4, previousScreen);
        this.onConfirm = onConfirm;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredTextWithShadow(
                matrices,
                this.textRenderer,
                Text.of("Are you sure? It's irreversible"),
                this.windowCenterHorizontal(),
                this.windowCenterVertical(),
                0xFFFFFF
        );
    }

    @Override
    protected void init() {
        super.init();
        this.yes = ButtonWidget.builder(
                Text.of("Yes"),
                b -> this.onConfirm.accept(this)
        ).dimensions(0, 0, 0, 20).build();
        this.no = ButtonWidget.builder(
                Text.of("No"),
                b -> this.close()
        ).dimensions(0, 0, 0, 20).build();

        ResizableWindow.alignButtons(
                ImmutableList.of(this.yes, this.no),
                this.getWindowLeft() + 5,
                this.getWindowBottom() - 25,
                this.getWindowWidth() - 10
        );
        this.addDrawableChild(this.yes);
        this.addDrawableChild(this.no);
    }
}
