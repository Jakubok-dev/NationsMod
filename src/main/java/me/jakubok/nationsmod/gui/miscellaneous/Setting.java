package me.jakubok.nationsmod.gui.miscellaneous;

import java.util.ArrayList;
import java.util.List;

import me.jakubok.nationsmod.gui.SimpleWindow;
import me.jakubok.nationsmod.gui.TabWindow;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class Setting {
    public String property, value;
    public final Subscreen<TabWindow> changeScreen;
    public MinecraftClient client;
    public final ButtonWidget changeButton;
    private int y;
    public final boolean changable;
    
    public void render(MatrixStack matrices, SimpleWindow parentScreen, TextRenderer textRenderer, int mouseX, int mouseY, float delta) {
        Screen.drawTextWithShadow(
            matrices,
            textRenderer,
            Text.of(property),
            SimpleWindow.windowLeft + 10,
            y,
            0xFFFFFF
        );
        
        Screen.drawCenteredText(
            matrices,
            textRenderer,
            Text.of(value),
            SimpleWindow.windowCenterHorizontal,
            y,
            0xFFFFFF
        );

    }

    class ChangeScreen extends TabWindow {

        public Setting instance;
        public ChangeScreen(Setting instance) {
            super(Text.of("Changing"));
            this.instance = instance;
        }

        @Override
        protected List<Subscreen<TabWindow>> getTabs() {
            List<Subscreen<TabWindow>> res = new ArrayList<>();
            res.add(changeScreen);
            return res;
        }
    }

    public Setting(String property, String value, Subscreen<TabWindow> changeScreen, MinecraftClient client, int y, boolean changable) {
        this.property = property;
        this.value = value;
        this.changeScreen = changeScreen;
        this.client = client;
        this.y = y;

        this.changable = changable;

        changeButton = new ButtonWidget(
            SimpleWindow.windowRight - 60,
            y - 6,
            50, 
            20, 
            Text.of("Change"), 
            t -> {
                this.client.setScreen(new ChangeScreen(this));
            }
        );
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
        this.changeButton.y = y;
    }
}
