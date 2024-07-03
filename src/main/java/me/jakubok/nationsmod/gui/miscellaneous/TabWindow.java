package me.jakubok.nationsmod.gui.miscellaneous;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class TabWindow extends ResizableWindow {

    protected final Identifier TABS_TEXTURE = new Identifier("textures/gui/container/creative_inventory/tabs.png");
    protected int selectedTab = 0;

    public List<Drawable> drawables = new ArrayList<>();

    public int getTabCountPerSide() {
        return this.getWindowWidth() / 28;
    }

    public TabWindow(Text title, int width, int height, int borderRadius, Screen previousScreen) {
        super(title, width, height, borderRadius, previousScreen);
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
        this.drawBackground(matrices, this.getWindowLeft(), this.getWindowTop(), this.windowWidth, this.windowHeight, this.getBorderRadius(), 0, 0, 4, 16, 16);
        drawCenteredTextWithShadow(
                matrices,
                this.textRenderer,
                this.title,
                this.windowCenterHorizontal(),
                this.getWindowTop() + 10,
                0xffffff
        );
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

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
        this.reload();
    }

    public void reload() {
        this.drawables.clear();
        this.clearChildren();
        this.init();
    }

    protected boolean isClickInTab(int index, double mouseX, double mouseY) {
        if (index / (this.getTabCountPerSide() + 1) == 0)
            return mouseX >= this.getWindowLeft() - 22 + 28*index &&
            mouseX <= this.getWindowLeft() + 6 + 28*index &&
            mouseY >= this.getWindowTop() - 28 &&
            mouseY <= this.getWindowTop() + 4;
        else 
            return mouseX >= this.getWindowLeft() - 22 + 28*(index - this.getTabCountPerSide()) &&
            mouseX <= this.getWindowLeft() + 6 + 28*(index - this.getTabCountPerSide()) &&
            mouseY >= this.getWindowBottom() - 3 &&
            mouseY <= this.getWindowBottom() + 25;
    }

    protected void drawASelectedTab(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, this.TABS_TEXTURE);

        this.renderTabIcon(matrices, this.getTabs().get(selectedTab), selectedTab + 1, true);

        if (this.getTabs().get(this.selectedTab).render != null)
            this.getTabs().get(this.selectedTab).render.render(matrices, mouseX, mouseY, delta, this);
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
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderTexture(0, this.TABS_TEXTURE);
            if (i != selectedTab) {
                this.renderTabIcon(matrices, this.getTabs().get(i), i + 1, false);
            }
        }
    }

    protected void renderTabIcon(MatrixStack matrices, Subscreen<TabWindow> option, int iteration, boolean selected) {

        int textureHeight;
        if (selected && iteration / (this.getTabCountPerSide() + 1) == 0)
            textureHeight = 32;
        else if (selected && iteration / (this.getTabCountPerSide() + 1) != 0)
            textureHeight = 96;
        else if (!selected && iteration / (this.getTabCountPerSide() + 1) == 0)
            textureHeight = 0;
        else
            textureHeight = 64;

        int height;
        if (iteration / (this.getTabCountPerSide() + 1) == 0)
            height = this.getWindowTop() - 28;
        else
            height = this.getWindowBottom() - 3;

        int width;
        if (iteration / (this.getTabCountPerSide() + 1) == 0)
             width = this.getWindowLeft() - 22 + 28*iteration;
        else
            width = this.getWindowLeft() - 22 + 28*(iteration - this.getTabCountPerSide());

        drawTexture(
            matrices, 
            width, 
            height, 
            26, 
            textureHeight, 
            26, 
            32
        );
        
        ItemStack itemStack = option.icon;
        this.itemRenderer.renderInGuiWithOverrides(
            matrices,
            itemStack, 
            width + 5, 
            height + 8
        );
        this.itemRenderer.renderGuiItemOverlay(
            matrices,
            this.textRenderer, 
            itemStack, 
            width + 5,
            height + 8
        );
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

