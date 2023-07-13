package me.jakubok.nationsmod.gui;

import me.jakubok.nationsmod.geometry.Polygon;
import me.jakubok.nationsmod.gui.miscellaneous.SimpleWindow;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class PolygonScreen extends SimpleWindow {

    protected Polygon polygon;
    protected ButtonWidget close, remove, select;
    protected boolean selected;
    protected final int index;

    public PolygonScreen(Screen previousScreen, Polygon polygon, int index, boolean selected) {
        super(Text.of("Polygon screen - " + polygon.name), previousScreen);
        this.index = index;
        this.selected = selected;
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
                Text.of("Select"),
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
        this.remove = ButtonWidget.builder(
                Text.of("Remove"),
                t -> {/* this.client.setScreen(new BorderSlotDeletionScreen(this, slot.name, this)) */}
        ).dimensions(
                (windowLeft + (windowRight - windowLeft) / 3) + 1,
                windowBottom - 25,
                (windowRight - windowLeft) / 3 - 2,
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
