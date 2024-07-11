package me.jakubok.nationsmod.gui.townScreen;

import com.google.common.collect.ImmutableList;
import me.jakubok.nationsmod.gui.townsScreen.TownsListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.*;

public class DistrictsListWidget extends ElementListWidget<DistrictsListWidget.DistrictEntry> {
    public final Map<String, UUID> districts;
    String search = "";
    public final Screen parentScreen;

    public DistrictsListWidget(MinecraftClient minecraftClient, Map<String, UUID> districts, int x, int width, int height, int top, int bottom, int itemHeight, Screen parentScreen) {
        super(minecraftClient, width, height, top, bottom, itemHeight);
        this.left = x;
        this.right += x;
        this.setRenderBackground(false);
        this.setRenderHorizontalShadows(false);
        this.districts = districts;
        this.parentScreen = parentScreen;
        this.refresh();
    }

    @Override
    protected int getScrollbarPositionX() {
        return this.right - 6;
    }

    protected void refresh() {
        this.clearEntries();
        List<String> keyList = new ArrayList<>(districts.keySet().stream().toList());
        Collections.sort(keyList);
        for (String district : keyList) {
            if (district.contains(this.search))
                this.addEntry(new DistrictEntry(districts.get(district), district));
        }
    }

    public void onSearchChange(String search) {
        this.search = search;
        this.refresh();
    }

    public static class DistrictEntry extends ElementListWidget.Entry<DistrictEntry> {
        public ButtonWidget button;
        public UUID districtID;
        public String districtName;

        public DistrictEntry(UUID districtID, String districtName) {
            this.districtID = districtID;
            this.districtName = districtName;
            this.button = ButtonWidget.builder(Text.of(districtName), c -> {}).dimensions(0, 0, 0, 20).build();
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(button);
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(button);
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.button.setX(x + entryWidth / 6);
            this.button.setY(y);
            this.button.setWidth(2 * entryWidth / 3 + 7);
            this.button.render(matrices, mouseX, mouseY, tickDelta);
        }
    }
}
