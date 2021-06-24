package me.Jakubok.nations.gui;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import me.Jakubok.nations.Nations;
import me.Jakubok.nations.block.NationPillarEntity;
import me.Jakubok.nations.util.GUIs;
import me.Jakubok.nations.util.Items;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TownCreationDescription extends SyncedGuiDescription {
    public TownCreationDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(GUIs.TOWN_CREATION, syncId, playerInventory, getBlockInventory(context, 1), getBlockPropertyDelegate(context));

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(140, 115);

        WLabel title = new WLabel(new TranslatableText("nationsmod.town_creation_gui.title"));
        title.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //title.setVerticalAlignment(VerticalAlignment.CENTER);

        WLabel name = new WLabel(new TranslatableText("nationsmod.town_creation_gui.name_label"));
        name.setVerticalAlignment(VerticalAlignment.CENTER);

        WTextField nameField = new WTextField();

        WLabel districtName = new WLabel(new TranslatableText("nationsmod.town_creation_gui.district_name_label"));
        districtName.setVerticalAlignment(VerticalAlignment.CENTER);

        WTextField districtNameField = new WTextField();
        districtNameField.setText("Center");

        WButton submit = new WButton(new TranslatableText("nationsmod.town_creation_gui.submit"));
        submit.setOnClick(() -> {
            World world = playerInventory.player.getEntityWorld();
            BlockPos pos = getPos(playerInventory);

            if (world == null || pos == null) {
                close(playerInventory.player);
                return;
            }

            if (!(world.getBlockEntity(pos) instanceof NationPillarEntity)) {
                close(playerInventory.player);
                return;
            }
            NationPillarEntity entity = (NationPillarEntity)world.getBlockEntity(pos);

            if (nameField.getText().equals("")) {
                close(playerInventory.player);
                return;
            }
            if (districtNameField.getText().equals("")) {
                districtNameField.setText("Center");
            }

            if (playerInventory.player.getMainHandStack().getItem() != Items.HEART_OF_NATION) return;

            if (entity.institutions.town == null) {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBlockPos(getPos(playerInventory));
                NbtCompound tag = new NbtCompound();
                tag.putString("title", nameField.getText());
                tag.putString("districtName", districtNameField.getText());
                buf.writeNbt(tag);
                ClientPlayNetworking.send(new Identifier(Nations.MOD_ID, "create_nation_by_player"), buf);
                submit.setEnabled(false);
            }

            close(playerInventory.player);
        });

        root.add(title, 5, 0, 2, 4);
        root.add(name, 0, 2);
        root.add(nameField, 5, 2, 7, 4);
        root.add(districtName, 0, 3);
        root.add(districtNameField, 5, 3, 7, 4);
        root.add(submit, 0, 5, 12, 5);
        root.validate(this);
    }

    protected static BlockPos getPos(PlayerInventory inv) {
        MinecraftClient client = MinecraftClient.getInstance();
        HitResult hit = client.crosshairTarget;

        if (hit.getType() != HitResult.Type.BLOCK) return null;
        BlockHitResult blockHit = (BlockHitResult) hit;
        return blockHit.getBlockPos();
    }
}
