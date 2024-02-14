package me.jakubok.nationsmod.gui.townScreen;

import java.util.Arrays;
import java.util.List;

import me.jakubok.nationsmod.administration.nation.Nation;
import me.jakubok.nationsmod.administration.province.Province;
import me.jakubok.nationsmod.gui.miscellaneous.Property;
import me.jakubok.nationsmod.gui.miscellaneous.SimpleWindow;
import me.jakubok.nationsmod.gui.miscellaneous.Subscreen;
import me.jakubok.nationsmod.gui.miscellaneous.TabWindow;
import me.jakubok.nationsmod.networking.ClientNetworking;
import me.jakubok.nationsmod.networking.Packets;
import me.jakubok.nationsmod.registries.ItemRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class GeneralInfoSubscreen {
    public final Subscreen<TabWindow> subscreen;

    public final List<Property> properties;
    int page = 0;
    public final ButtonWidget settingsUp, settingsDown; 
    public Province province; public Nation nation;

    public GeneralInfoSubscreen(TownScreen inst) {
        this.subscreen = new Subscreen<TabWindow>(Text.of("General info"), new ItemStack(ItemRegistry.TOWN_INDEPENDENCE_DECLARATION), this::render, this::init);

        if (inst.town.getProvincesID() == null) {
            province = null; nation = null;
        } else {
            this.getTheProvince(inst);
        }
        
        this.properties = Arrays.asList(
            new Property(
                Text.of("Name:"),
                Text.of(inst.town.getName()),
                inst.getClient(),
                SimpleWindow.windowTop + 35
            ),
            new Property(
                Text.of("Government:"),
                inst.town.formOfGovernment.getDisplayName(),
                inst.getClient(),
                SimpleWindow.windowTop + 35 + 21
            ),
            new Property(
                Text.of("Citizens:"),
                Text.of(inst.town.getAIMembers().size() + inst.town.getPlayerMembers().size() + ""),
                inst.getClient(),
                SimpleWindow.windowTop + 35 + 21 * 2
            ),
            new Property(
                Text.of("Districts:"),
                Text.of(inst.town.getTheListOfDistrictsIDs().size() + ""),
                inst.getClient(),
                SimpleWindow.windowTop + 35 + 21 * 3
            ),
            new Property(
                Text.of("Province:"),
                Text.of("-"),
                inst.getClient(),
                SimpleWindow.windowTop + 35 + 21 * 4
            ),
            new Property(
                Text.of("Nation:"),
                Text.of("-"),
                inst.getClient(),
                SimpleWindow.windowTop + 35
            ),
            new Property(
                Text.of("Petition support:"),
                Text.of(inst.town.getThePetitionSupport() + "%"),
                inst.getClient(),
                SimpleWindow.windowTop + 35 + 21
            ),
            new Property(
                Text.of("Citizenship:"),
                inst.town.getTheCitizenshipApprovement().displayText,
                inst.getClient(),
                SimpleWindow.windowTop + 35 + 21 * 2
            )
        );

        settingsUp = ButtonWidget.builder(
            Text.of("▲"), 
            t -> {
                page--;
                inst.reload();
            }
        ).dimensions(
            SimpleWindow.windowLeft + 5, 
            SimpleWindow.windowTop + 5, 
            20, 
            20
        ).build();

        settingsDown = ButtonWidget.builder(
            Text.of("▼"), 
            t -> {
                page++;
                inst.reload();
            }
        ).dimensions(
            SimpleWindow.windowLeft + 5, 
            SimpleWindow.windowBottom - 25, 
            20, 
            20
        ).build();
    }

    protected void getTheProvince(TownScreen inst) {
        PacketByteBuf buffer = PacketByteBufs.create();
        NbtCompound nbt = new NbtCompound();
        nbt.putUuid("id", inst.town.getProvincesID());
        buffer.writeNbt(nbt);
        ClientNetworking.makeARequest(Packets.GET_A_PROVINCE, buffer,
        (MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) -> {
            NbtCompound nbtresponse = buf.readNbt();
            client.execute(() -> {
                this.province = new Province(nbtresponse, null);
                this.properties.get(4).value = Text.of(this.province.getName());
                this.getTheNation(inst);
            });
        });
    }

    protected void getTheNation(TownScreen inst) {
        PacketByteBuf buffer = PacketByteBufs.create();
        NbtCompound nbt = new NbtCompound();
        nbt.putUuid("id", this.province.getNationsUUID());
        buffer.writeNbt(nbt);
        ClientNetworking.makeARequest(Packets.GET_A_NATION, buffer,
        (MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) -> {
            NbtCompound nbtresponse = buf.readNbt();
            client.execute(() -> {
                this.nation = new Nation(nbtresponse, null);
                this.properties.get(5).value = Text.of(this.nation.getName());
            });
        });
    }

    protected void render(MatrixStack matrices, int mouseX, int mouseY, float delta, TabWindow instance) {
        for (int i = page*5; i < 5*(page + 1) && i < this.properties.size(); i++) {
            this.properties.get(i).render(matrices, instance, instance.getTextRenderer(), mouseX, mouseY, delta);
        }
    }

    protected void init(TabWindow instance) {
        this.settingsUp.active = isUpActive();
        this.settingsDown.active = isDownActive();
        instance.addDrawableChild(this.settingsUp);
        instance.addDrawableChild(this.settingsDown);
        for (int i = page*5; i < 5*(page + 1) && i < this.properties.size(); i++) {
            this.properties.get(i).client = instance.getClient();
        }
    }

    protected void remove(TabWindow instance) {
        instance.drawables.clear();
    }

    public boolean isUpActive() {
        return this.page > 0;
    }

    public boolean isDownActive() {
        return (this.page + 1) * 5 < this.properties.size();
    }
}
