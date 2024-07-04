package me.jakubok.nationsmod.gui.polygonsStorageScreen;

import com.google.common.collect.ImmutableList;
import me.jakubok.nationsmod.collection.Pair;
import me.jakubok.nationsmod.geometry.Polygon;
import me.jakubok.nationsmod.gui.PolygonScreen;
import me.jakubok.nationsmod.networking.ClientNetworking;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;

public class PolygonsListWidget extends ElementListWidget<PolygonsListWidget.PolygonEntry> {
    public final Map<String, Integer> polygons;
    String search = "";
    public final Screen parentScreen;
    public final int selectedSlot;
    public PolygonsListWidget(MinecraftClient minecraftClient, Map<String, Integer> polygons, int selectedSlot, int x, int width, int height, int top, int bottom, int itemHeight, Screen parentScreen) {
        super(minecraftClient, width, height, top, bottom, itemHeight);
        this.left = x;
        this.right += x;
        this.setRenderBackground(false);
        this.setRenderHorizontalShadows(false);
        this.polygons = polygons;
        this.parentScreen = parentScreen;
        this.selectedSlot = selectedSlot;
        this.refresh();
    }

    @Override
    protected int getScrollbarPositionX() {
        return this.right - 6;
    }

    protected void refresh() {
        this.clearEntries();
        List<String> keyList = new ArrayList<>(polygons.keySet().stream().toList());
        keyList.sort((a, b) -> {
            if (this.polygons.get(a) == this.selectedSlot)
                return -1;
            if (this.polygons.get(b) == this.selectedSlot)
                return 1;
            return a.compareTo(b);
        });
        for (String polygon : keyList) {
            if (polygon.contains(this.search))
                this.addEntry(new PolygonEntry(polygon, this.polygons.get(polygon)));
        }
        this.addEntry(new PolygonEntry("+", -1));
    }

    public void onSearchChange(String search) {
        this.search = search;
        this.refresh();
    }

    public class PolygonEntry extends ElementListWidget.Entry<PolygonEntry> {
        String polygonName; int polygonSlot;
        ButtonWidget btn;

        public PolygonEntry(String polygonName, int polygonSlot) {
            MutableText message = Text.literal(polygonName);
            if (polygonSlot == PolygonsListWidget.this.selectedSlot && polygonSlot != -1)
                message.formatted(Formatting.BOLD);
            this.btn = ButtonWidget.builder(message, c -> {
                PacketByteBuf buffer = PacketByteBufs.create();
                buffer.writeInt(polygonSlot);

                ClientPlayNetworking.PlayChannelHandler response = (MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) -> {
                    Polygon polygon = new Polygon(buf.readNbt());
                    boolean selected = buf.readBoolean();

                    client.execute(() -> client.setScreen(new PolygonScreen(PolygonsListWidget.this.parentScreen, polygon, polygonSlot, selected)));
                };

                ClientNetworking.makeARequest(Packets.GET_A_POLYGON, buffer, response);
            }).dimensions(0, 0, 0, 20).build();
            this.polygonName = polygonName;
            this.polygonSlot = polygonSlot;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(btn);
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(btn);
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.btn.setX(x + entryWidth / 6);
            this.btn.setY(y);
            this.btn.setWidth(2 * entryWidth / 3 + 7);
            this.btn.render(matrices, mouseX, mouseY, tickDelta);
        }
    }
}
