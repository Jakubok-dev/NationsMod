package me.jakubok.nationsmod.gui.townScreen;

import com.mojang.blaze3d.systems.RenderSystem;
import me.jakubok.nationsmod.gui.miscellaneous.Subscreen;
import me.jakubok.nationsmod.gui.miscellaneous.TabWindow;
import me.jakubok.nationsmod.networking.ClientNetworking;
import me.jakubok.nationsmod.networking.Packets;
import me.jakubok.nationsmod.registries.ItemRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DistrictsSubscreen {
    public final Subscreen<TabWindow> subscreen;
    protected TextFieldWidget searchBox;
    protected Map<String, UUID> districts = new HashMap<>();
    protected DistrictsListWidget districtsListWidget;
    public final TownScreen inst;

    public DistrictsSubscreen(TownScreen inst) {
        this.subscreen = new Subscreen<>(
                Text.of("Districts"),
                new ItemStack(ItemRegistry.DISTRICT_DECLARATION),
                this::render,
                this::init
        );
        this.inst = inst;
    }

    protected void init(TabWindow instance) {
        assert this.inst.getClient() != null;

        NbtCompound nbt = new NbtCompound();
        AtomicInteger size = new AtomicInteger(0);
        for (UUID id : inst.town.getTheListOfDistrictsIDs())
            nbt.putUuid("id" + size.getAndIncrement(), id);
        nbt.putInt("size", size.get());
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeNbt(nbt);
        ClientPlayNetworking.PlayChannelHandler response = (client, handler, buf, responseSender) -> {
            NbtCompound receivedNbt = buf.readNbt();
            for (int i = 0; i < Objects.requireNonNull(receivedNbt).getInt("size"); i++)
                this.districts.put(receivedNbt.getString("key" + i), receivedNbt.getUuid("value" + i));
            if (this.inst.getTabs().get(this.inst.getSelectedTab()) != this.subscreen)
                return;
            this.districtsListWidget = new DistrictsListWidget(
                    this.inst.getClient(),
                    this.districts,
                    this.inst.getWindowLeft(),
                    this.inst.getWindowWidth() - 5,
                    this.inst.act == null ? this.inst.getWindowHeight() - 52 : this.inst.getWindowHeight() - 77,
                    this.inst.getWindowTop() + 25,
                    this.inst.act == null ? this.inst.getWindowBottom() - 27 : this.inst.getWindowBottom() - 52,
                    25,
                    this.inst
            );
            this.inst.addDrawableChild(this.districtsListWidget);

            this.searchBox = new TextFieldWidget(
                    this.inst.getClient().textRenderer,
                    this.inst.windowCenterHorizontal() - 56,
                    this.inst.act == null ? this.inst.getWindowBottom() - 25 : this.inst.getWindowBottom() - 50,
                    133,
                    20,
                    Text.of("")
            );
            this.searchBox.setPlaceholder(Text.literal("Search...").formatted(Formatting.ITALIC).formatted(Formatting.GRAY));
            this.searchBox.setChangedListener(this.districtsListWidget::onSearchChange);
            this.inst.addDrawableChild(this.searchBox);
        };
        ClientNetworking.makeARequest(Packets.GET_DISTRICTS_NAMES, buffer, response);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, TabWindow instance) {
        RenderSystem.setShaderTexture(0, new Identifier("textures/gui/social_interactions.png"));
        DrawableHelper.drawTexture(matrices, this.inst.windowCenterHorizontal() - 73, this.inst.act == null ? this.inst.getWindowBottom() - 21 : this.inst.getWindowBottom() - 46, 243, 1, 12, 12);
    }
}
