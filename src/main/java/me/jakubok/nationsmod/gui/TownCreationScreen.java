package me.jakubok.nationsmod.gui;


import me.jakubok.nationsmod.gui.miscellaneous.SimpleWindow;
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

public class TownCreationScreen extends SimpleWindow {

    protected TextFieldWidget townName;
    protected TextFieldWidget mainDistrictName;
    protected ButtonWidget submit;

    protected MinecraftClient client;

    public TownCreationScreen(MinecraftClient client, Screen previousScreen) {
        super(Text.translatable("gui.nationsmod.town_creation_screen.title"), previousScreen);
        this.client = client;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);

        // Town name:
        drawCenteredTextWithShadow(
            matrices,
            textRenderer, 
            Text.translatable("gui.nationsmod.town_creation_screen.town_name"), 
            windowCenterHorizontal - 75, 
            windowCenterVertical - 20, 
            0xffffff
        );

        // District name:
        drawCenteredTextWithShadow(
            matrices,
            textRenderer, 
            Text.translatable("gui.nationsmod.town_creation_screen.district_name"), 
            windowCenterHorizontal - 75, 
            windowCenterVertical + 5, 
            0xffffff
        );
    }

    @Override
	protected void init() {
        super.init();
        
        this.townName = new TextFieldWidget(
            textRenderer,
            windowCenterHorizontal,
            windowCenterVertical - 25,
            100,
            20,
            Text.translatable("gui.nationsmod.town_creation_screen.town_name")
        );
        this.addDrawableChild(townName);

        this.mainDistrictName = new TextFieldWidget(
            textRenderer, 
            windowCenterHorizontal, 
            windowCenterVertical,
            100, 
            20, 
            Text.translatable("gui.nationsmod.town_creation_screen.district_name")
        );
        this.addDrawableChild(mainDistrictName);

        this.submit = ButtonWidget.builder(
            Text.translatable("gui.nationsmod.submit"), 
            b -> {

                if (this.mainDistrictName.getText().length() <= 0 || this.townName.getText().length() <= 0)
                    return;

                PacketByteBuf buf = PacketByteBufs.create();

                NbtCompound compound = new NbtCompound();
                compound.putString("town_name", this.townName.getText());
                compound.putString("district_name", this.mainDistrictName.getText());

                buf.writeNbt(compound);

                ClientPlayNetworking.send(Packets.CREATE_A_TOWN, buf);

                b.active = false;
                this.client.setScreen(null);
            }
        ).dimensions(
            windowCenterHorizontal - 64, 
            windowBottom - 25, 
            128, 
            20
        ).build();
        this.addDrawableChild(this.submit);
    }
}
