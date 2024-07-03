package me.jakubok.nationsmod.gui.townsScreen;


import java.util.Map;
import java.util.UUID;

import com.mojang.blaze3d.systems.RenderSystem;
import me.jakubok.nationsmod.gui.miscellaneous.ResizableWindow;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class TownsScreen extends ResizableWindow {

    protected TextFieldWidget searchBox;
    protected Map<String, UUID> towns;
    protected TownsListWidget townsListWidget;

    public TownsScreen(Map<String, UUID> towns, Screen previousScreen) {
        super(Text.translatable("gui.nationsmod.towns_screen.title"), previousScreen);
        this.towns = towns;
    }

    @Override
	protected void init() {
        super.init();

        this.townsListWidget = new TownsListWidget(
                this.client,
                this.towns,
                this.getWindowLeft(),
                this.getWindowWidth() - 5,
                this.getWindowHeight() - 52,
                this.getWindowTop() + 25,
                this.getWindowBottom() - 27,
                25,
                this
        );
        this.addDrawableChild(this.townsListWidget);

        this.searchBox = new TextFieldWidget(
            this.textRenderer, 
            this.windowCenterHorizontal() - 56,
            this.getWindowBottom() - 25,
            133,
            20, 
            Text.of("")
        );
        this.searchBox.setPlaceholder(Text.literal("Search...").formatted(Formatting.ITALIC).formatted(Formatting.GRAY));
        this.searchBox.setChangedListener(this.townsListWidget::onSearchChange);
        this.addDrawableChild(this.searchBox);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        RenderSystem.setShaderTexture(0, new Identifier("textures/gui/social_interactions.png"));
        drawTexture(matrices, this.windowCenterHorizontal() - 73, this.getWindowBottom() - 21, 243, 1, 12, 12);
    }
}
