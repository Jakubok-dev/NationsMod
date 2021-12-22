package me.jakubok.nationsmod.gui.townScreen;

import java.util.Arrays;
import java.util.List;

import me.jakubok.nationsmod.administration.Town;
import me.jakubok.nationsmod.gui.SimpleWindow;
import me.jakubok.nationsmod.gui.TabWindow;
import me.jakubok.nationsmod.gui.miscellaneous.Setting;
import me.jakubok.nationsmod.gui.miscellaneous.Subscreen;
import me.jakubok.nationsmod.registries.ItemRegistry;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class Constitution {
    public final Subscreen<TabWindow> subscreen;

    public final List<Setting> settings;
    int page = 0;
    public final ButtonWidget settingsUp, settingsDown, petitionSubmit;

    public Constitution(TownScreen inst) {
        this.subscreen = new Subscreen<TabWindow>(
            Text.of("Constitution"),
            new ItemStack(ItemRegistry.CONSTITUTION), 
            (MatrixStack matrices, int mouseX, int mouseY, float delta, TabWindow instance) -> render(matrices, mouseX, mouseY, delta, instance),
            instance -> init(instance), 
            instance -> remove(instance)
        );

        settings = Arrays.asList(new Setting[] {
            new Setting(
                "Name:", 
                inst.town.getName(), 
                new Subscreen<TabWindow>(
                    Text.of("Town's name"),
                    new ItemStack(ItemRegistry.CONSTITUTION), 
                    (MatrixStack matrices, int mouseX, int mouseY, float delta, TabWindow instance) -> {
                        SimpleWindow.drawCenteredText(
                            matrices, 
                            inst.getTextRenderer(), 
                            Text.of("n?"), 
                            SimpleWindow.windowCenterHorizontal, 
                            SimpleWindow.windowCenterVertical, 
                            0xFFFFFF
                        );
                    }, 
                    (instance) -> {}, 
                    null
                ), 
                inst.getClient(),
                SimpleWindow.windowTop + 35 + 21 * 0,
                true
            ),
            new Setting(
                "Mayor:", 
                inst.town.getARuler().name, 
                new Subscreen<TabWindow>(
                    Text.of("Mayor"),
                    new ItemStack(ItemRegistry.CONSTITUTION), 
                    (MatrixStack matrices, int mouseX, int mouseY, float delta, TabWindow instance) -> {
                        SimpleWindow.drawCenteredText(
                            matrices, 
                            inst.getTextRenderer(), 
                            Text.of("m?"), 
                            SimpleWindow.windowCenterHorizontal, 
                            SimpleWindow.windowCenterVertical, 
                            0xFFFFFF
                        );
                    }, 
                    (instance) -> {}, 
                    null
                ), 
                inst.getClient(),
                SimpleWindow.windowTop + 35 + 21 * 1,
                true
            ),
            new Setting(
                "Regime:", 
                "dictatorship", 
                new Subscreen<TabWindow>(
                    Text.of("Regime"),
                    new ItemStack(ItemRegistry.CONSTITUTION), 
                    (MatrixStack matrices, int mouseX, int mouseY, float delta, TabWindow instance) -> {
                        SimpleWindow.drawCenteredText(
                            matrices, 
                            inst.getTextRenderer(), 
                            Text.of("r?"), 
                            SimpleWindow.windowCenterHorizontal, 
                            SimpleWindow.windowCenterVertical, 
                            0xFFFFFF
                        );
                    }, 
                    (instance) -> {}, 
                    null
                ), 
                inst.getClient(),
                SimpleWindow.windowTop + 35 + 21 * 2,
                true
            ),
            new Setting(
                "Citizens:", 
                inst.town.getCitizens().size() + "", 
                null, 
                inst.getClient(),
                SimpleWindow.windowTop + 35 + 21 * 3,
                false
            ),
            new Setting(
                "Districts:", 
                inst.town.getDistricts().size() + "", 
                null, 
                inst.getClient(),
                SimpleWindow.windowTop + 35 + 21 * 4,
                false
            ),
            new Setting(
                "Petition support:", 
                "10 %", 
                new Subscreen<TabWindow>(
                    Text.of("Regime"),
                    new ItemStack(ItemRegistry.CONSTITUTION), 
                    (MatrixStack matrices, int mouseX, int mouseY, float delta, TabWindow instance) -> {
                        SimpleWindow.drawCenteredText(
                            matrices, 
                            inst.getTextRenderer(), 
                            Text.of("ps?"), 
                            SimpleWindow.windowCenterHorizontal, 
                            SimpleWindow.windowCenterVertical, 
                            0xFFFFFF
                        );
                    }, 
                    (instance) -> {}, 
                    null
                ),  
                inst.getClient(),
                SimpleWindow.windowTop + 35 + 21 * 0,
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
                this.subscreen.remove.remove(inst);
                page--;
                this.subscreen.init.init(inst);
            }
        );

        settingsDown = new ButtonWidget(
            SimpleWindow.windowLeft + 5, 
            SimpleWindow.windowBottom - 25, 
            20, 
            20, 
            Text.of("▼"), 
            t -> {
                this.subscreen.remove.remove(inst);
                page++;
                this.subscreen.init.init(inst);
            }
        );

        petitionSubmit = new ButtonWidget(
            SimpleWindow.windowLeft + 30, 
            SimpleWindow.windowBottom - 25, 
            210,
            20, 
            Text.of("Create a petition"),
            t -> {}
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
        instance.addDrawableChild(this.petitionSubmit);
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
