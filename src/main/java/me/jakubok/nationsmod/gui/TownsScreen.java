package me.jakubok.nationsmod.gui;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class TownsScreen extends SimpleWindow {

    protected ButtonWidget left, right;
    protected TextFieldWidget searchBox;

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
