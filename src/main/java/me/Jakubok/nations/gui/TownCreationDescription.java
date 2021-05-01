package me.Jakubok.nations.gui;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import me.Jakubok.nations.administration.Town;
import me.Jakubok.nations.block.NationPillarEntity;
import me.Jakubok.nations.util.Blocks;
import me.Jakubok.nations.util.GUIs;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TownCreationDescription extends SyncedGuiDescription {
    public TownCreationDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, World world, BlockPos pos) {
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
        if (world != null && pos != null) {
            submit.setOnClick(new Runnable() {
                @Override
                public void run() {
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
                        entity.institutions.town = new Town(nameField.getText(), pos, playerInventory.player, null, world);
                        playerInventory.player.sendMessage(Text.of("Created town " + entity.institutions.town.name), false);
                    }

                    close(playerInventory.player);
                }
            });
        }

        root.add(title, 3, 0);
        root.add(name, 0, 2);
        root.add(nameField, 2, 2, 6, 5);
        root.add(submit, 0, 4, 8, 5);
        root.validate(this);
    }
}
