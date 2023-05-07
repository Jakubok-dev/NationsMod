package me.jakubok.nationsmod.gui.miscellaneous;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class TabWindow extends SimpleWindow {

    protected final Identifier TABS_TEXTURE = new Identifier("textures/gui/container/creative_inventory/tabs.png");
    protected int selectedTab = 0;

    public List<Drawable> drawables = new ArrayList<>();

    public TabWindow(Text title, Screen previousScreen) {
        super(title, previousScreen);
    }

    protected abstract List<Subscreen<TabWindow>> getTabs();

    @Override
    protected void init() {
        super.init();

        if (this.getTabs().get(this.selectedTab).init != null) 
            this.getTabs().get(this.selectedTab).init.init(this);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        this.drawUnselectedTabs(matrices, delta, mouseX, mouseY);
        this.drawBackground(matrices);
        this.drawASelectedTab(matrices, mouseX, mouseY, delta);
        this.drawWidgets(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {

        for (int i = 0; i < this.getTabs().size(); i++) {
            if (this.isClickInTab(i + 1, mouseX, mouseY)) {
                this.selectedTab = i;
                this.reload();
            }
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    public void reload() {
        this.drawables.clear();
        this.clearChildren();
        this.init();
    }

    protected boolean isClickInTab(int index, double mouseX, double mouseY) {
        if (index / 9 == 0)
            return mouseX >= windowLeft - 22 + 29*index &&
            mouseX <= windowLeft + 6 + 29*index &&
            mouseY >= windowTop - 28 &&
            mouseY <= windowTop + 4;
        else 
            return mouseX >= windowLeft - 22 + 29*(index % 8) &&
            mouseX <= windowLeft + 6 + 29*(index % 8) &&
            mouseY >= windowBottom - 3 &&
            mouseY <= windowBottom + 25;
    }

    protected void drawASelectedTab(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.TABS_TEXTURE);

        this.renderTabIcon(matrices, this.getTabs().get(selectedTab), selectedTab + 1, true);

        if (this.getTabs().get(this.selectedTab).render != null)
            this.getTabs().get(this.selectedTab).render.render(matrices, mouseX, mouseY, delta, this);
    }

    protected void drawBackground(MatrixStack matrices) {
        RenderSystem.setShaderTexture(0, new Identifier("minecraft", "textures/gui/demo_background.png"));

        drawTexture(matrices, 120, 50, 0, 0, 256, 256, 256, 256);

        drawCenteredText(
            matrices, 
            this.textRenderer, 
            Text.of(this.title.asString() + " - " + this.getTabs().get(this.selectedTab).name.asString()), 
            windowCenterHorizontal,
            windowTop + 10,
            0xffffff
        );
    }

    protected void drawWidgets(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Iterator<Drawable> var5 = this.drawables.iterator();

        while(var5.hasNext()) {
            Drawable drawable = (Drawable)var5.next();
            drawable.render(matrices, mouseX, mouseY, delta);
        }
    }

    protected void drawUnselectedTabs(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        for(int i = 0; i < this.getTabs().size(); i++) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, this.TABS_TEXTURE);
            if (i != selectedTab) {
                this.renderTabIcon(matrices, this.getTabs().get(i), i + 1, false);
            }
        }
    }

    protected void renderTabIcon(MatrixStack matrices, Subscreen<TabWindow> option, int iteration, boolean selected) {

        int textureHeight;
        if (selected && iteration / 9 == 0)
            textureHeight = 32;
        else if (selected && iteration / 9 != 0)
            textureHeight = 96;
        else if (!selected && iteration / 9 == 0)
            textureHeight = 0;
        else
            textureHeight = 64;

        int height;
        if (iteration / 9 == 0)
            height = windowTop - 28;
        else
            height = windowBottom - 3;

        int width;
        if (iteration / 9 == 0)
            width = windowLeft - 22 + 29*iteration;
        else
            width = windowLeft - 22 + 29*(iteration % 8);

        this.drawTexture(
            matrices, 
            width, 
            height, 
            28, 
            textureHeight, 
            28, 
            32
        );

        this.itemRenderer.zOffset = 100.0F;
        ItemStack itemStack = option.icon;
        this.itemRenderer.renderInGuiWithOverrides(
            itemStack, 
            width + 6, 
            height + 8
        );
        this.itemRenderer.renderGuiItemOverlay(
            this.textRenderer, 
            itemStack, 
            width + 6,
            height + 8
        );
        this.itemRenderer.zOffset = 0.0F;
    } 

    @Override
    public <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement) {
        this.drawables.add(drawableElement);
        return super.addDrawableChild(drawableElement);
    }

    @Override
    public void remove(Element child) {
        if (child instanceof Drawable) {
            this.drawables.remove((Drawable)child);
        }
        super.remove(child);
    }
}

