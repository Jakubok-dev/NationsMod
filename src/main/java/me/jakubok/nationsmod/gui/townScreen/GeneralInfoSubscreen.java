package me.jakubok.nationsmod.gui.townScreen;

import java.util.Arrays;
import java.util.List;

import me.jakubok.nationsmod.gui.miscellaneous.ChangeOfASettingScreen;
import me.jakubok.nationsmod.gui.miscellaneous.Setting;
import me.jakubok.nationsmod.gui.miscellaneous.SimpleWindow;
import me.jakubok.nationsmod.gui.miscellaneous.Subscreen;
import me.jakubok.nationsmod.gui.miscellaneous.TabWindow;
import me.jakubok.nationsmod.registries.ItemRegistry;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class GeneralInfoSubscreen {
    public final Subscreen<TabWindow> subscreen;

    public final List<Setting> settings;
    int page = 0;
    public final ButtonWidget settingsUp, settingsDown; 

    public GeneralInfoSubscreen(TownScreen inst) {
        this.subscreen = new Subscreen<TabWindow>(
            Text.of("General info"),
            new ItemStack(ItemRegistry.TOWN_INDEPENDENCE_DECLARATION), 
            (MatrixStack matrices, int mouseX, int mouseY, float delta, TabWindow instance) -> render(matrices, mouseX, mouseY, delta, instance),
            instance -> init(instance)
        );

        settings = Arrays.asList(new Setting[] {
            new Setting(
                "Name:", 
                inst.town.getName(), 
                new ChangeOfASettingScreen(
                    Text.of("Name"), 
                    instance -> {},
                    inst
                ), 
                inst.getClient(),
                SimpleWindow.windowTop + 35 + 21 * 0,
                true
            ),
            new Setting(
                "Government:", 
                inst.town.formOfGovernment.getDisplayName().getString(), 
                new ChangeOfASettingScreen(
                    Text.of("Form of government:"), 
                    instance -> {},
                    inst
                ),
                inst.getClient(),
                SimpleWindow.windowTop + 35 + 21 * 1,
                true
            ),
            new Setting(
                "Citizens:", 
                inst.town.getAIMembers().size() + inst.town.getPlayerMembers().size() + "", 
                null, 
                inst.getClient(),
                SimpleWindow.windowTop + 35 + 21 * 2,
                false
            ),
            new Setting(
                "Districts:", 
                inst.town.getDistricts().size() + "", 
                null, 
                inst.getClient(),
                SimpleWindow.windowTop + 35 + 21 * 3,
                false
            ),
            new Setting(
                "Petition support:", 
                "10 %", 
                new ChangeOfASettingScreen(
                    Text.of("Petition support"), 
                    instance -> {},
                    inst
                ),
                inst.getClient(),
                SimpleWindow.windowTop + 35 + 21 * 4,
                true
            )
        });

        settingsUp = new ButtonWidget(
            SimpleWindow.windowLeft + 5, 
            SimpleWindow.windowTop + 5, 
            20, 
            20, 
            Text.of("▲"), 
            t -> {
                page--;
                inst.reload();
            }
        );

        settingsDown = new ButtonWidget(
            SimpleWindow.windowLeft + 5, 
            SimpleWindow.windowBottom - 25, 
            20, 
            20, 
            Text.of("▼"), 
            t -> {
                page++;
                inst.reload();
            }
        );
    }

    protected void render(MatrixStack matrices, int mouseX, int mouseY, float delta, TabWindow instance) {
        for (int i = page*5; i < 5*(page + 1) && i < this.settings.size(); i++) {
            this.settings.get(i).render(matrices, instance, instance.getTextRenderer(), mouseX, mouseY, delta);
        }
    }

    protected void init(TabWindow instance) {
        this.settingsUp.active = isUpActive();
        this.settingsDown.active = isDownActive();
        instance.addDrawableChild(this.settingsUp);
        instance.addDrawableChild(this.settingsDown);
        for (int i = page*5; i < 5*(page + 1) && i < this.settings.size(); i++) {
            this.settings.get(i).client = instance.getClient();
            if (this.settings.get(i).changable)
                instance.addDrawableChild(this.settings.get(i).changeButton);
        }
    }

    protected void remove(TabWindow instance) {
        instance.drawables.clear();
    }

    public boolean isUpActive() {
        return this.page > 0;
    }

    public boolean isDownActive() {
        return (this.page + 1) * 5 < this.settings.size();
    }
}
