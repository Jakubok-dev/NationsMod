package me.Jakubok.nations.gui;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import me.Jakubok.nations.Nations;
import me.Jakubok.nations.block.NationPillarEntity;
import me.Jakubok.nations.util.GUIs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundTag;
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
        root.setSize(100, 100);

        WLabel title = new WLabel(new TranslatableText("nationsmod.town_creation_gui.title"));
        title.setHorizontalAlignment(HorizontalAlignment.CENTER);
        title.setSize(5, 6);

        WLabel name = new WLabel(new TranslatableText("nationsmod.town_creation_gui.name_label"));
        name.setVerticalAlignment(VerticalAlignment.CENTER);

        WTextField nameField = new WTextField();

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

            if (nameField.getText() == "") {
                close(playerInventory.player);
                return;
            }

            if (entity.institutions.town == null) {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBlockPos(getPos(playerInventory));
                CompoundTag tag = new CompoundTag();
                tag.putString("title", nameField.getText());
                buf.writeCompoundTag(tag);
                ClientPlayNetworking.send(new Identifier(Nations.MOD_ID, "create_nation_by_player"), buf);
            }

            close(playerInventory.player);
        });

        root.add(title, 3, 0);
        root.add(name, 0, 2);
        root.add(nameField, 2, 2, 6, 5);
        root.add(submit, 0, 4, 8, 5);
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
