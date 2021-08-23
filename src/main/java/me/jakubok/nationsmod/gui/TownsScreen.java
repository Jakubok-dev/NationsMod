package me.jakubok.nationsmod.gui;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class TownsScreen extends Screen {

    protected ButtonWidget left, right;
    protected TextFieldWidget searchBox;

    protected final int windowLeft = 120;
    protected final int windowRight = windowLeft + 248;
    
    protected final int windowTop = 50;
    protected final int windowBottom = windowTop + 165;

    protected final int windowCenterHorizontal = (windowLeft + windowRight) / 2;
    protected final int windowCenterVertical = (windowTop + windowBottom) / 2;

    protected Map<String, UUID> towns = new HashMap<>();
    protected List<String> townsNames = new ArrayList<>();
    protected List<String> filteredTownsNames = new ArrayList<>();
    protected List<ButtonWidget> townButtons = new ArrayList<>();

    protected int page = 0;
    protected boolean isPageAtLeft() { return page > 0;  }
    protected boolean isPageAtRight() { return (page+1)*4 < this.filteredTownsNames.size(); }
    
    public TownsScreen(Map<String, UUID> towns) {
        super(new TranslatableText("gui.nationsmod.towns_screen.title"));
        this.towns = towns;
        townsNames.addAll(towns.keySet());
        Collections.sort(townsNames);
        this.filteredTownsNames = townsNames;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        RenderSystem.setShaderTexture(0, new Identifier("minecraft", "textures/gui/demo_background.png"));

        drawTexture(matrices, 120, 50, 0, 0, 256, 256, 256, 256);

        // Towns
        drawCenteredText(
            matrices, 
            this.textRenderer, 
            this.title, 
            windowCenterHorizontal, 
            windowTop + 10,
            0xffffff
        );

        super.render(matrices, mouseX, mouseY, delta);
    }

    protected void drawTowns() {
        this.left.active = isPageAtLeft();
        this.right.active = isPageAtRight();

        townButtons.forEach(el -> {
            this.remove(el);
        });
        townButtons.clear();

        for (int i = 1; i <= 4 && (page*4)+i <= this.filteredTownsNames.size(); i++) {
            townButtons.add(new ButtonWidget(
                this.windowCenterHorizontal - 73,
                this.windowTop + 28*i,
                150,
                20,
                Text.of(filteredTownsNames.get((page*4) + i - 1)),
                b -> {}
            ));
            this.addDrawableChild(townButtons.get(i-1));
        }
    }

    @Override
	protected void init() {
        super.init();

        this.searchBox = new TextFieldWidget(
            this.textRenderer, 
            this.windowCenterHorizontal - 73,
            this.windowBottom - 25,
            150, 
            20, 
            Text.of("")
        );
        this.searchBox.setChangedListener(text -> {
            this.page = 0;

            this.filteredTownsNames = this.townsNames.stream()
                .filter(el -> el.contains(text))
                .toList();

            this.drawTowns();
        });
        this.addDrawableChild(this.searchBox);
        
        this.left = new ButtonWidget(
            this.windowLeft + 5,
            this.windowCenterVertical - 10, 
            20, 
            20, 
            Text.of("<"), 
            b -> {
                page--;
                this.drawTowns();
            }
        );
        this.addDrawableChild(this.left);

        this.right = new ButtonWidget(
            this.windowRight - 25,
            this.windowCenterVertical - 10, 
            20, 
            20, 
            Text.of(">"), 
            b -> {
                page++;
                this.drawTowns();
            }
        );
        this.addDrawableChild(this.right);

        this.drawTowns();
    }
}
