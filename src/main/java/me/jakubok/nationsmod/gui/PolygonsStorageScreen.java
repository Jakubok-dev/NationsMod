package me.jakubok.nationsmod.gui;

import me.jakubok.nationsmod.geometry.Polygon;
import me.jakubok.nationsmod.gui.miscellaneous.SimpleWindow;
import me.jakubok.nationsmod.networking.ClientNetworking;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PolygonsStorageScreen extends SimpleWindow {

    public final Map<String, Integer> storage;

    protected ButtonWidget left, right;
    protected TextFieldWidget searchBox;

    protected int page = 0;
    protected boolean isPageAtLeft() { return page > 0;  }
    protected boolean isPageAtRight() { return (page+1)*4 < this.filteredSlotsNames.size(); }

    List<String> slotsNames = new ArrayList<>();
    List<String> filteredSlotsNames = new ArrayList<>();
    List<ButtonWidget> slotsButtons = new ArrayList<>();

    public PolygonsStorageScreen(Map<String, Integer> storage, Screen previousScreen) {
        super(Text.of("Polygons storage screen"), previousScreen);
        this.storage = storage;
        this.slotsNames.addAll(this.storage.keySet());
        Collections.sort(this.slotsNames);

        this.storage.put("+", -1);
        this.slotsNames.add("+");

        this.filteredSlotsNames = this.slotsNames;
    }

    protected void drawSlots() {
        this.left.active = isPageAtLeft();
        this.right.active = isPageAtRight();

        this.slotsButtons.forEach(this::remove);
        this.slotsButtons.clear();

        for (int i = 1; i <= 4 && (page*4)+i <= this.filteredSlotsNames.size(); i++) {
            final int temp = i;

            this.slotsButtons.add(ButtonWidget.builder(
                    Text.of(this.filteredSlotsNames.get((page*4) + i - 1)),
                    b -> {
                        PacketByteBuf buffer = PacketByteBufs.create();
                        buffer.writeInt(this.storage.get(this.filteredSlotsNames.get((page*4) + temp - 1)));


                        ClientPlayNetworking.PlayChannelHandler response = (MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) -> {
                            Polygon polygon = new Polygon(buf.readNbt());
                            boolean selected = buf.readBoolean();

                            client.execute(() -> {
                                client.setScreen(new PolygonScreen(this, polygon, this.storage.get(this.filteredSlotsNames.get((page*4) + temp - 1)), selected));
                            });
                        };

                        ClientNetworking.makeARequest(Packets.GET_A_POLYGON, buffer, response);
                    }
            ).dimensions(
                    windowCenterHorizontal - 73,
                    windowTop + 28*i,
                    150,
                    20
            ).build());
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

        this.left = ButtonWidget.builder(
                Text.of("<"),
                b -> {
                    page--;
                    this.drawSlots();
                }
        ).dimensions(
                windowLeft + 5,
                windowCenterVertical - 10,
                20,
                20
        ).build();
        this.addDrawableChild(this.left);

        this.right = ButtonWidget.builder(
                Text.of(">"),
                b -> {
                    page++;
                    this.drawSlots();
                }
        ).dimensions(
                windowRight - 25,
                windowCenterVertical - 10,
                20,
                20
        ).build();
        this.addDrawableChild(this.right);
        this.drawSlots();
    }
}
