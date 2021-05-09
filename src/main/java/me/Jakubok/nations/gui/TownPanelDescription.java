package me.Jakubok.nations.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import me.Jakubok.nations.administration.Town;
import me.Jakubok.nations.util.Items;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class TownPanelDescription extends LightweightGuiDescription {

    public TownPanelDescription(Town town, PlayerEntity entity) {
        if (town == null) return;
        WTabPanel tabs = new WTabPanel();
        setRootPanel(tabs);
        tabs.setSize(470, 260);

        Text titleText = Text.of(new TranslatableText("nationsmod.town_panel_gui.title").getString() + town.name);
        WLabel title = new WLabel(titleText);
        title.setHorizontalAlignment(HorizontalAlignment.CENTER);

        WGridPanel panel = new WGridPanel();
        panel.setSize(454, 215);
        panel.add(title, 10, 0, 4, 4);
        panel.validate(this);

        tabs.add(panel, tab -> tab.icon(new ItemIcon(new ItemStack(Items.CONSTITUTION))));
        tabs.validate(this);

    }
}
