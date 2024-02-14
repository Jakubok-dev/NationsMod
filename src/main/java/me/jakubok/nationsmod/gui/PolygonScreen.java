package me.jakubok.nationsmod.gui;

import me.jakubok.nationsmod.geometry.Polygon;
import me.jakubok.nationsmod.gui.miscellaneous.Property;
import me.jakubok.nationsmod.gui.miscellaneous.SimpleWindow;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PolygonScreen extends SimpleWindow {

    protected Polygon polygon;
    protected ButtonWidget close, remove, select;
    protected boolean selected;
    protected final int index;

    public List<Property> properties = new ArrayList<>();

    public PolygonScreen(Screen previousScreen, Polygon polygon, int index, boolean selected) {
        super(Text.of("Polygon screen - " + polygon.name), previousScreen);
        this.index = index;
        this.selected = selected;
        this.polygon = polygon;
        this.properties.add(
            new Property(
                Text.of("Nodes"),
                Text.of(String.valueOf(this.polygon.size())),
                this.getClient(),
                SimpleWindow.windowTop + 35
            )
        );

        if (this.polygon.getDomain() != null) {
            this.properties.add(new Property(
                    Text.of("Min X"),
                    Text.of(String.valueOf(this.polygon.getDomain().from)),
                    this.getClient(),
                    SimpleWindow.windowTop + 35 + 21 * this.properties.size()
            ));
            this.properties.add(new Property(
                    Text.of("Max X"),
                    Text.of(String.valueOf(this.polygon.getDomain().to)),
                    this.getClient(),
                    SimpleWindow.windowTop + 35 + 21 * this.properties.size()
            ));
        }

        if (this.polygon.getValueSet() != null) {
            this.properties.add(new Property(
                    Text.of("Min Y"),
                    Text.of(String.valueOf(this.polygon.getValueSet().from)),
                    this.getClient(),
                    SimpleWindow.windowTop + 35 + 21 * this.properties.size()
            ));
            this.properties.add(new Property(
                    Text.of("Max Y"),
                    Text.of(String.valueOf(this.polygon.getValueSet().to)),
                    this.getClient(),
                    SimpleWindow.windowTop + 35 + 21 * this.properties.size()
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
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        for (int i = 0; i < 5 && i < this.properties.size(); i++) {
            this.properties.get(i).render(matrices, this, this.getTextRenderer(), mouseX, mouseY, delta);
        }
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
                (windowLeft) + 5,
                windowBottom - 25,
                (windowRight - windowLeft) / 3 - 5,
                20
        ).build();
        this.addDrawableChild(this.select);
        this.remove = ButtonWidget.builder(
                Text.of("Remove"),
                t -> { this.client.setScreen(new PolygonDeletionScreen(this.index, null)); }
        ).dimensions(
                (windowLeft + (windowRight - windowLeft) / 3) + 1,
                windowBottom - 25,
                (windowRight - windowLeft) / 3,
                20
        ).build();
        this.addDrawableChild(this.remove);

        this.close = ButtonWidget.builder(
                Text.of("Close"),
                t -> this.close()
        ).dimensions(
                (windowLeft + ((windowRight - windowLeft) / 3)*2) + 2,
                windowBottom - 25,
                (windowRight - windowLeft) / 3 - 5,
                20
        ).build();
        this.addDrawableChild(this.close);
    }
}
