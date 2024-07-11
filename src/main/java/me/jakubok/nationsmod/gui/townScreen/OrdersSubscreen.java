package me.jakubok.nationsmod.gui.townScreen;

import com.google.common.collect.ImmutableList;
import me.jakubok.nationsmod.administration.town.TownLawDescription;
import me.jakubok.nationsmod.gui.miscellaneous.Subscreen;
import me.jakubok.nationsmod.gui.miscellaneous.TabWindow;
import me.jakubok.nationsmod.gui.miscellaneous.property.CheckBoxPropertyEntry;
import me.jakubok.nationsmod.gui.miscellaneous.property.PropertyEntry;
import me.jakubok.nationsmod.gui.miscellaneous.property.PropertyListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

import java.util.List;

public class OrdersSubscreen {
    public final Subscreen<TabWindow> subscreen;
    public final TownScreen inst;
    public PropertyListWidget propertyListWidget;
    public List<PropertyEntry> propertyEntries;

    public OrdersSubscreen(TownScreen inst) {
        this.subscreen = new Subscreen<>(
                Text.of("Orders"),
                new ItemStack(Items.GOAT_HORN),
                this::render,
                this::init
        );
        this.inst = inst;
    }

    protected void render(MatrixStack matrices, int mouseX, int mouseY, float delta, TabWindow instance) {
    }

    protected void init(TabWindow instance) {
        this.propertyEntries = ImmutableList.of(
                new CheckBoxPropertyEntry(
                        this.inst.getClient(),
                        Text.of("Disband the town"),
                        this.inst.act.getOrders().get(TownLawDescription.DisbandOrderLabel) != null,
                        c -> {
                            if (c.isChecked()) {
                                this.inst.act.order(TownLawDescription.DisbandOrderLabel, new NbtCompound());
                                return;
                            }
                            this.inst.act.removeAnOrder(TownLawDescription.DisbandOrderLabel);
                        }
                )
        );
        this.propertyListWidget = new PropertyListWidget(
                instance.getClient(),
                this.propertyEntries,
                instance.getWindowLeft(),
                instance.getWindowWidth() - 5,
                instance.getWindowHeight() - 55,
                instance.getWindowTop() + 25,
                instance.getWindowBottom() - 30,
                25
        );
        instance.addDrawableChild(this.propertyListWidget);
    }
}
