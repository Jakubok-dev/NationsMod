package me.jakubok.nationsmod.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.jakubok.nationsmod.collections.BorderGroup;
import me.jakubok.nationsmod.gui.miscellaneous.SimpleWindow;
import me.jakubok.nationsmod.networking.ClientNetworking;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayChannelHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

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

    public BorderRegistratorScreen(Map<String, Integer> slots, Screen previousScreen) {
        super(new TranslatableText("gui.nationsmod.border_registrator_screen.title"), previousScreen);
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
                windowCenterHorizontal - 73,
                windowTop + 28*i,
                150,
                20,
                Text.of(this.filteredSlotsNames.get((page*4) + i - 1)),
                b -> {
                    PacketByteBuf buffer = PacketByteBufs.create();
                    buffer.writeInt(this.slots.get(this.filteredSlotsNames.get((page*4) + temp - 1)));
                    
                    PlayChannelHandler response = (MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) -> {
                        BorderGroup slot = new BorderGroup(buf.readNbt());
                        boolean selected = buf.readBoolean();

                        client.execute(() -> {
                            client.setScreen(new BorderSlotScreen(slot, selected, this));
                        });
                    };

                    ClientNetworking.makeARequest(Packets.PREPARE_BORDER_SLOT_SCREEN, buffer, response);
                }
            ));
            this.addDrawableChild(this.slotsButtons.get(i-1));
        }
    }

    @Override
	protected void init() {
        super.init();

        this.searchBox = new TextFieldWidget(
            textRenderer, 
            windowCenterHorizontal - 73,
            windowBottom - 25,
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
            windowLeft + 5,
            windowCenterVertical - 10, 
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
            windowRight - 25,
            windowCenterVertical - 10, 
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
