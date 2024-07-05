package me.jakubok.nationsmod.gui.townsScreen;

import com.google.common.collect.ImmutableList;
import me.jakubok.nationsmod.administration.town.Town;
import me.jakubok.nationsmod.gui.townScreen.TownScreen;
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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import java.util.*;

public class TownsListWidget extends ElementListWidget<TownsListWidget.TownEntry> {

    public final Map<String, UUID> towns;
    String search = "";
    public final Screen parentScreen;

    public TownsListWidget(MinecraftClient minecraftClient, Map<String, UUID> towns, int x, int width, int height, int top, int bottom, int itemHeight, Screen parentScreen) {
        super(minecraftClient, width, height, top, bottom, itemHeight);
        this.left = x;
        this.right += x;
        this.setRenderBackground(false);
        this.setRenderHorizontalShadows(false);
        this.towns = towns;
        this.parentScreen = parentScreen;
        this.refresh();
    }

    @Override
    protected int getScrollbarPositionX() {
        return this.right - 6;
    }

    protected void refresh() {
        this.clearEntries();
        List<String> keyList = new ArrayList<>(towns.keySet().stream().toList());
        Collections.sort(keyList);
        for (String town : keyList) {
            if (town.contains(this.search))
                this.addEntry(new TownEntry(towns.get(town), town));
        }
    }

    public void onSearchChange(String search) {
        this.search = search;
        this.refresh();
    }

    public class TownEntry extends ElementListWidget.Entry<TownEntry> {
        public ButtonWidget button;
        public UUID townID;
        public String townName;

        public TownEntry(UUID townID, String townName) {
            this.townID = townID;
            this.townName = townName;
            this.button = ButtonWidget.builder(Text.of(townName), c -> {
                PacketByteBuf buffer = PacketByteBufs.create();
                NbtCompound nbt = new NbtCompound();
                nbt.putUuid("id", this.townID);
                buffer.writeNbt(nbt);

                ClientPlayNetworking.PlayChannelHandler response = (MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) -> {
                    Town town = new Town(buf.readNbt(), null);

                    client.execute(() -> client.setScreen(new TownScreen(town, TownsListWidget.this.parentScreen, null)));
                };
                ClientNetworking.makeARequest(Packets.PREPARE_TOWN_SCREEN, buffer, response);
            }).dimensions(0, 0, 0, 20).build();
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(button);
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(button);
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.button.setX(x + entryWidth / 6);
            this.button.setY(y);
            this.button.setWidth(2 * entryWidth / 3 + 7);
            this.button.render(matrices, mouseX, mouseY, tickDelta);
        }
    }
}
