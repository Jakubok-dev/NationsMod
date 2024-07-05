package me.jakubok.nationsmod.gui;

import com.google.common.collect.ImmutableList;
import me.jakubok.nationsmod.collection.Pair;
import me.jakubok.nationsmod.gui.miscellaneous.form.FormListWidget;
import me.jakubok.nationsmod.gui.miscellaneous.form.FormWindow;
import me.jakubok.nationsmod.gui.miscellaneous.form.RadioButtonInput;
import me.jakubok.nationsmod.gui.miscellaneous.form.TextInput;
import me.jakubok.nationsmod.networking.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActCreationScreen extends FormWindow {
    protected Pair<UUID, String> town;
    protected Pair<UUID, String> nation;
    public ActCreationScreen(Pair<UUID, String> town, Pair<UUID, String> nation, Screen previousScreen) {
        super(Text.of("Act creation"), 225, 150, 4, previousScreen);
        this.town = town;
        this.nation = nation;
    }

    @Override
    protected void init() {
        List<FormListWidget.FormInputEntry> desc = new ArrayList<>();
        desc.add(new TextInput(
            Text.of("Name:"),
            Text.literal("Write..."),
            o -> {
                if (!(o instanceof String str))
                    return Text.literal("ERROR, Object is not an instance of string").formatted(Formatting.RED);
                if (str.trim().equals(""))
                    return Text.literal("Input is empty!").formatted(Formatting.RED);
                return Text.of("");
            },
            this.client
        ));
        if (this.nation != null) {
            desc.add(new RadioButtonInput(
                    ImmutableList.of(
                            Text.of("Institution"),
                            Text.of(this.town.value),
                            Text.of(this.nation.value)
                    ),
                    o -> {
                        if ((int)o < 0)
                            return Text.literal("No option has been selected!").formatted(Formatting.RED);
                        return Text.of("");
                    },
                    this.client
            ));
        }
        this.fillInTheDescription(
                desc,
                l -> {
                    NbtCompound nbt = new NbtCompound();
                    nbt.putString("name", (String)l.get(0));
                    if (l.size() > 1) {
                        int choice = (int)l.get(1);
                        if (choice == 0)
                            nbt.putUuid("bodyID", this.town.key);
                        else
                            nbt.putUuid("bodyID", this.nation.key);
                    } else
                        nbt.putUuid("bodyID", this.town.key);
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeNbt(nbt);
                    ClientPlayNetworking.send(Packets.CREATE_AN_ACT, buf);
                    assert this.client != null;
                    this.client.setScreen(null);
                }
        );
        super.init();
    }
}
