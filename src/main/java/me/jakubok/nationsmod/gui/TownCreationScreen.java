package me.jakubok.nationsmod.gui;


import com.mojang.blaze3d.systems.RenderSystem;

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
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class TownCreationScreen extends Screen {

    protected TextFieldWidget townName;
    protected TextFieldWidget mainDistrictName;
    protected ButtonWidget submit;

    protected final int windowLeft = 128;
    protected final int windowRight = windowLeft + 248;
    
    protected final int windowTop = 50;
    protected final int windowBottom = windowTop + 165;

    protected final int windowCenterHorizontal = (windowLeft + windowRight) / 2;
    protected final int windowCenterVertical = (windowTop + windowBottom) / 2;

    protected MinecraftClient client;

    public TownCreationScreen(MinecraftClient client) {
        super(new TranslatableText("gui.nationsmod.town_creation_screen.title"));
        this.client = client;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        RenderSystem.setShaderTexture(0, new Identifier("minecraft", "textures/gui/demo_background.png"));

        drawTexture(matrices, 128, 50, 0, 0, 256, 256, 256, 256);

        // Town creation screen
        drawCenteredText(
            matrices, 
            this.textRenderer, 
            this.title, 
            windowCenterHorizontal, 
            windowTop + 10,
            0xffffff
        );

        // Town name:
        drawCenteredText(
            matrices,
            textRenderer, 
            new TranslatableText("gui.nationsmod.town_creation_screen.town_name"), 
            windowCenterHorizontal - 75, 
            windowCenterVertical - 20, 
            0xffffff
        );

        // District name:
        drawCenteredText(
            matrices,
            textRenderer, 
            new TranslatableText("gui.nationsmod.town_creation_screen.district_name"), 
            windowCenterHorizontal - 75, 
            windowCenterVertical + 5, 
            0xffffff
        );

        super.render(matrices, mouseX, mouseY, delta);
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
            new TranslatableText("gui.nationsmod.town_creation_screen.town_name")
        );
        this.addDrawableChild(townName);

        this.mainDistrictName = new TextFieldWidget(
            textRenderer, 
            windowCenterHorizontal, 
            windowCenterVertical,
            100, 
            20, 
            new TranslatableText("gui.nationsmod.town_creation_screen.district_name")
        );
        this.addDrawableChild(mainDistrictName);

        this.submit = new ButtonWidget(
            windowCenterHorizontal - 64, 
            windowBottom - 25, 
            128, 
            20, 
            new TranslatableText("gui.nationsmod.town_creation_screen.submit"), 
            b -> {

                if (this.mainDistrictName.getText().length() <= 0 || this.townName.getText().length() <= 0)
                    return;

                PacketByteBuf buf = PacketByteBufs.create();

                NbtCompound compound = new NbtCompound();
                compound.putString("town_name", this.townName.getText());
                compound.putString("district_name", this.mainDistrictName.getText());

                buf.writeNbt(compound);

                ClientPlayNetworking.send(Packets.CREATE_A_TOWN_PACKET, buf);

                b.active = false;
                this.client.setScreen(null);
            }
        );
        this.addDrawableChild(this.submit);
    }
}
