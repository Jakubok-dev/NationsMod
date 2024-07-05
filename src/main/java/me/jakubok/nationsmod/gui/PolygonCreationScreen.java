package me.jakubok.nationsmod.gui;

import com.google.common.collect.ImmutableList;
import me.jakubok.nationsmod.gui.miscellaneous.ResizableWindow;
import me.jakubok.nationsmod.gui.miscellaneous.form.BasicValidations;
import me.jakubok.nationsmod.gui.miscellaneous.form.FormWindow;
import me.jakubok.nationsmod.gui.miscellaneous.form.TextInput;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class PolygonCreationScreen extends FormWindow {

    public PolygonCreationScreen(Screen previousScreen) {
        super(
                Text.of("Polygon creation"),
                230,
                85,
                4,
                previousScreen
        );
    }

    @Override
    protected void init() {
        this.fillInTheDescription(
                ImmutableList.of(
                        new TextInput(
                                Text.of("Name:"),
                                Text.literal("Write..."),
                                o -> {
                                    Text res = BasicValidations.BASIC_STRING_VALIDATION(o);
                                    if (!res.getString().equals(""))
                                        return res;
                                    if (((String)o).trim().equals("+"))
                                        return Text.literal("The name must not be \"+\"!").formatted(Formatting.RED);
                                    return Text.of("");
                                },
                                this.client
                        )
                ),
                l -> {
                    String name = (String)l.get(0);
                    PacketByteBuf buffer = PacketByteBufs.create();
                    buffer.writeString(name);

                    ClientPlayNetworking.send(Packets.CREATE_A_POLYGON, buffer);

                    assert this.client != null;
                    this.client.setScreen(null);
                }
        );
        super.init();
    }
}
