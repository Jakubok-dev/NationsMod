package me.jakubok.nationsmod.gui;


import com.google.common.collect.ImmutableList;
import me.jakubok.nationsmod.gui.miscellaneous.ResizableWindow;
import me.jakubok.nationsmod.gui.miscellaneous.form.FormWindow;
import me.jakubok.nationsmod.gui.miscellaneous.form.TextInput;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TownCreationScreen extends FormWindow {

    protected MinecraftClient client;

    public TownCreationScreen(MinecraftClient client, Screen previousScreen) {
        super(
                Text.translatable("gui.nationsmod.town_creation_screen.title"),
                230,
                110,
                4,
                previousScreen
        );
        this.client = client;
    }

    @Override
    protected void init() {
        this.fillInTheDescription(
                ImmutableList.of(
                        new TextInput(
                                Text.translatable("gui.nationsmod.town_creation_screen.town_name"),
                                Text.literal("..."),
                                o -> {
                                    if (!(o instanceof String str))
                                        return Text.literal("ERROR, Object is not an instance of string").formatted(Formatting.RED);
                                    if (str.trim().equals(""))
                                        return Text.literal("Input is empty!").formatted(Formatting.RED);
                                    return Text.of("");
                                },
                                this.client
                        ),
                        new TextInput(
                                Text.translatable("gui.nationsmod.town_creation_screen.district_name"),
                                Text.literal("..."),
                                o -> {
                                    if (!(o instanceof String str))
                                        return Text.literal("ERROR, Object is not an instance of string").formatted(Formatting.RED);
                                    if (str.trim().equals(""))
                                        return Text.literal("Input is empty!").formatted(Formatting.RED);
                                    return Text.of("");
                                },
                                this.client
                        )
                ),
                l -> {
                    PacketByteBuf buf = PacketByteBufs.create();

                    NbtCompound compound = new NbtCompound();
                    compound.putString("town_name", (String) l.get(0));
                    compound.putString("district_name", (String) l.get(1));

                    buf.writeNbt(compound);

                    ClientPlayNetworking.send(Packets.CREATE_A_TOWN, buf);

                    this.client.setScreen(null);
                }
        );
        super.init();
    }
}
