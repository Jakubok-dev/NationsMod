package me.jakubok.nationsmod.gui;

import me.jakubok.nationsmod.collection.Pair;
import me.jakubok.nationsmod.geometry.Polygon;
import me.jakubok.nationsmod.gui.miscellaneous.PropertyListWidget;
import me.jakubok.nationsmod.gui.miscellaneous.ResizableWindow;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class PolygonScreen extends ResizableWindow {

    protected Polygon polygon;
    protected ButtonWidget close, remove, select;
    protected boolean selected;
    protected final int index;

    public List<Pair<Text, Text>> properties = new ArrayList<>();
    public PropertyListWidget propertyListWidget;

    public PolygonScreen(Screen previousScreen, Polygon polygon, int index, boolean selected) {
        super(Text.of("Polygon screen - " + polygon.name), previousScreen);
        this.index = index;
        this.selected = selected;
        this.polygon = polygon;
        this.properties.add(
            new Pair<>(
                Text.of("Nodes"),
                Text.of(String.valueOf(this.polygon.size()))
            )
        );

        if (this.polygon.getDomain() != null) {
            this.properties.add(new Pair<>(
                    Text.of("Min X"),
                    Text.of(String.valueOf(this.polygon.getDomain().from))
            ));
            this.properties.add(new Pair<>(
                    Text.of("Max X"),
                    Text.of(String.valueOf(this.polygon.getDomain().to))
            ));
        }

        if (this.polygon.getValueSet() != null) {
            this.properties.add(new Pair<>(
                    Text.of("Min Y"),
                    Text.of(String.valueOf(this.polygon.getValueSet().from))
            ));
            this.properties.add(new Pair<>(
                    Text.of("Max Y"),
                    Text.of(String.valueOf(this.polygon.getValueSet().to))
            ));
        }
    }

    public void makeSelected() {
        this.selected = true;
        this.select.setMessage(Text.of("Unselect"));
    }

    public void makeUnselected() {
        this.selected = false;
        this.select.setMessage(Text.translatable("Select"));
    }

    @Override
    protected void init() {
        super.init();

        this.select = ButtonWidget.builder(
                this.selected ? Text.of("Unselect") : Text.of("Select"),
                t -> {
                    if (this.selected) {

                        ClientPlayNetworking.send(Packets.UNSELECT_A_POLYGON, PacketByteBufs.create());
//                        NationsClient.selectedSlot = -1;
//                        NationsClient.borderSlot = new BorderGroup();

                        this.makeUnselected();
                        return;
                    }

                    PacketByteBuf buffer = PacketByteBufs.create();
                    buffer.writeInt(this.index);

//                    NationsClient.borderSlot = new BorderGroup();
                    ClientPlayNetworking.send(Packets.SELECT_A_POLYGON, buffer);
//                    NationsClient.selectedSlot = this.index;

                    this.makeSelected();
                }
        ).dimensions(
                this.getWindowLeft() + 5,
                this.getWindowBottom() - 25,
                (this.getWindowRight() - this.getWindowLeft()) / 3 - 5,
                20
        ).build();
        this.addDrawableChild(this.select);
        this.remove = ButtonWidget.builder(
                Text.of("Remove"),
                t -> {
                    assert this.client != null;
                    this.client.setScreen(new PolygonDeletionScreen(this.index, null));
                }
        ).dimensions(
                (this.getWindowLeft() + (this.getWindowRight() - this.getWindowLeft()) / 3) + 1,
                this.getWindowBottom() - 25,
                (this.getWindowRight() - this.getWindowLeft()) / 3,
                20
        ).build();
        this.addDrawableChild(this.remove);

        this.close = ButtonWidget.builder(
                Text.of("Close"),
                t -> this.close()
        ).dimensions(
                (this.getWindowLeft() + ((this.getWindowRight() - this.getWindowLeft()) / 3)*2) + 2,
                this.getWindowBottom() - 25,
                (this.getWindowRight() - this.getWindowLeft()) / 3 - 5,
                20
        ).build();
        this.addDrawableChild(this.close);

        this.propertyListWidget = new PropertyListWidget(
                this.client,
                this.properties,
                this.getWindowLeft(),
                this.getWindowWidth() - 5,
                this.getWindowHeight() - 30,
                this.getWindowTop() + 25,
                this.getWindowBottom() - 5,
                20
        );
        this.addDrawableChild(this.propertyListWidget);
    }
}
