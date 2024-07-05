package me.jakubok.nationsmod.gui.miscellaneous.form;

import me.jakubok.nationsmod.collection.Pair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FormListWidget extends ElementListWidget<FormListWidget.FormInputEntry> {

    public FormListWidget(MinecraftClient minecraftClient, List<FormInputEntry> description, int x, int width, int height, int top, int bottom, int itemHeight) {
        super(minecraftClient, width, height, top, bottom, itemHeight);
        this.left = x;
        this.right += x;
        this.setRenderBackground(false);
        this.setRenderHorizontalShadows(false);
        for (FormInputEntry entry : description)
            this.addEntry(entry);
    }

    @Override
    protected int getScrollbarPositionX() {
        return this.right - 6;
    }

    public List<Pair<Text, Text>> getValidationFeedback() {
        List<Pair<Text, Text>> res = new ArrayList<>();
        for (int i = 0; i < this.getEntryCount(); i++) {
            Text message = this.getEntry(i).getValidationFeedback();
            if (message == null)
                continue;
            if (message.getString().equals(""))
                continue;
            res.add(new Pair<>(this.getEntry(i).labels.get(0), message));
        }
        return res;
    }

    public boolean isValid() {
        for (int i = 0; i < this.getEntryCount(); i++) {
            if (!this.getEntry(i).isValid())
                return false;
        }
        return true;
    }

    public List<Object> getInputs() {
        List<Object> res = new ArrayList<>();
        for (int i = 0; i < this.getEntryCount(); i++) {
            res.add(this.getEntry(i).getInput());
        }
        return res;
    }

    public static abstract class FormInputEntry extends ElementListWidget.Entry<FormInputEntry> {
        protected Function<Object, Text> validateFunction;
        protected List<Text> labels;
        public final MinecraftClient client;

        FormInputEntry(List<Text> labels, Function<Object, Text> validateFunction, MinecraftClient client) {
            this.labels = labels;
            this.validateFunction = validateFunction;
            this.client = client;
        }

        public abstract Object getInput();
        public Text getValidationFeedback() {
            return this.validateFunction.apply(this.getInput());
        }
        public boolean isValid() {
            if (this.getValidationFeedback() == null)
                return true;
            return this.getValidationFeedback().getString().equals("");
        }
    }
}
