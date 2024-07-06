package me.jakubok.nationsmod.gui.miscellaneous.property;

import me.jakubok.nationsmod.administration.law.Act;
import me.jakubok.nationsmod.collection.Pair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.function.Function;

public class MutableTextProperty<T> extends TextProperty implements MutableProperty<T> {
    public final Function<T, Text> getProperty;
    public final Function<T, Text> getValue;
    final Pair<String, T> rule;
    final Act<?> act;
    final EditScreenFactory<T> factory;
    public ButtonWidget valueButton;

    public MutableTextProperty(MinecraftClient client, Pair<String, T> rule, Function<T, Text> getProperty, Function<T, Text> getValue, EditScreenFactory<T> onChange, Act<?> act) {
        super(client, getProperty.apply(rule.value), getValue.apply(rule.value));
        this.getProperty = getProperty;
        this.getValue = getValue;
        this.rule = rule;
        this.act = act;
        this.factory = onChange;
        this.valueButton = ButtonWidget.builder(
                this.value,
                b -> this.client.setScreen(this.factory.get(this, this.client))
        ).dimensions(0, 0, this.client.textRenderer.getWidth(this.value) + 4, 20).build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<? extends Selectable> selectableChildren() {
        List<Selectable> res = (List<Selectable>) super.selectableChildren();
        res.add(this.valueButton);
        return res;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<? extends Element> children() {
        List<Element> res = (List<Element>) super.children();
        res.add(this.valueButton);
        return res;
    }

    @Override
    public Pair<String, T> getTheRule() {
        return this.rule;
    }

    @Override
    public Act<?> getAct() {
        return this.act;
    }

    @Override
    public EditScreenFactory<T> getEditScreenFactory() {
        return this.factory;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        Text renderProp = this.property;
        Object obj = this.act.getARule(this.rule.key);
        if (obj != null) {
            T newRule = (T) obj;
            Text newProperty = this.getProperty.apply(newRule);
            Text newValue = this.getValue.apply(newRule);
            if (!this.property.equals(newProperty))
                renderProp = Text.literal(newProperty.getString()).formatted(Formatting.ITALIC).formatted(Formatting.UNDERLINE);
            if (!this.value.equals(newValue)) {
                this.valueButton.setMessage(Text.literal(newValue.getString()).formatted(Formatting.ITALIC).formatted(Formatting.UNDERLINE));
                this.valueButton.setWidth(this.client.textRenderer.getWidth(this.valueButton.getMessage()) + 4);
            }
        }

        Screen.drawTextWithShadow(
                matrices,
                this.client.textRenderer,
                renderProp,
                x + 2,
                y + (entryHeight - this.client.textRenderer.fontHeight) / 2,
                0xFFFFFF
        );

        this.valueButton.setX(x + entryWidth - this.valueButton.getWidth() - 7);
        this.valueButton.setY(y);
        this.valueButton.render(
                matrices,
                mouseX,
                mouseY,
                tickDelta
        );
    }

    public static <T> TextProperty of(MinecraftClient client, Pair<String, T> rule, Function<T, Text> getProperty, Function<T, Text> getValue, EditScreenFactory<T> onChange, Act<?> act) {
        return act == null
                ? new TextProperty(client, getProperty.apply(rule.value), getValue.apply(rule.value))
                : new MutableTextProperty<>(client, rule, getProperty, getValue, onChange, act);
    }
}
