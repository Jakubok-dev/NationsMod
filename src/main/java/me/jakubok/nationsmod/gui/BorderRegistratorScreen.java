package me.jakubok.nationsmod.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class BorderRegistratorScreen extends SimpleWindow {

    protected ButtonWidget left, right;
    protected TextFieldWidget searchBox;

    protected int page = 0;
    protected boolean isPageAtLeft() { return page > 0;  }
    protected boolean isPageAtRight() { return (page+1)*4 < this.filteredSlotsNames.size(); }

    Map<String, Integer> slots = new HashMap<>();
    List<String> slotsNames = new ArrayList<>();
    List<String> filteredSlotsNames = new ArrayList<>();
    List<ButtonWidget> slotsButtons = new ArrayList<>();

    public BorderRegistratorScreen(Map<String, Integer> slots) {
        super(Text.of("Border slots"));
        this.slots = slots;
        this.slotsNames.addAll(this.slots.keySet());
        Collections.sort(this.slotsNames);
        
        this.slots.put("+", -1);
        this.slotsNames.add("+");

        this.filteredSlotsNames = this.slotsNames;
    }

    protected void drawSlots() {
        this.left.active = isPageAtLeft();
        this.right.active = isPageAtRight();

        this.slotsButtons.forEach(el -> {
            this.remove(el);
        });
        this.slotsButtons.clear();

        for (int i = 1; i <= 4 && (page*4)+i <= this.filteredSlotsNames.size(); i++) {
            final int temp = i;

            this.slotsButtons.add(new ButtonWidget(
                this.windowCenterHorizontal - 73,
                this.windowTop + 28*i,
                150,
                20,
                Text.of(this.filteredSlotsNames.get((page*4) + i - 1)),
                b -> {
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeInt(this.slots.get(this.filteredSlotsNames.get((page*4) + temp - 1)));
                    
                    //ClientPlayNetworking.send(Packets.PREPARE_TOWN_SCREEN_PACKET, buf);
                }
            ));
            this.addDrawableChild(this.slotsButtons.get(i-1));
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

            this.filteredSlotsNames = this.slotsNames.stream()
                .filter(el -> el.contains(text) || el.equals("+"))
                .toList();

            this.drawSlots();
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
                this.drawSlots();
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
                this.drawSlots();
            }
        );
        this.addDrawableChild(this.right);

        this.drawSlots();
    }
}
