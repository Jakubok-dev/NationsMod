package me.jakubok.nationsmod.gui.miscellaneous.form;

import me.jakubok.nationsmod.gui.miscellaneous.ResizableWindow;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;

public abstract class FormWindow extends ResizableWindow {
    public ButtonWidget submit;
    public List<FormListWidget.FormInputEntry> formDescription;
    public Consumer<List<Object>> onSubmit;
    public FormListWidget formListWidget;

    public FormWindow(Text title, int width, int height, int borderRadius, Screen previousScreen) {
        super(title, width, height, borderRadius, previousScreen);
    }

    public void fillInTheDescription(List<FormListWidget.FormInputEntry> formDescription, Consumer<List<Object>> onSubmit) {
        this.formDescription = formDescription;
        this.onSubmit = onSubmit;
    }

    @Override
    protected void init() {
        super.init();
        this.formListWidget = new FormListWidget(
                this.client,
                this.formDescription,
                this.getWindowLeft(),
                this.getWindowWidth() - 5,
                this.getWindowHeight() - 52,
                this.getWindowTop() + 25,
                this.getWindowBottom() - 27,
                25
        );
        this.addDrawableChild(this.formListWidget);
        this.submit = ButtonWidget.builder(Text.of("Submit"), c -> {
            if (this.formListWidget.isValid()) {
                this.onSubmit.accept(this.formListWidget.getInputs());
                return;
            }
            assert this.client != null;
            this.client.setScreen(new InvalidFormWindow(
                    Text.of(this.title.getString() + " - " + "Invalid prompt"),
                    this.formListWidget.getValidationFeedback(),
                    this.getWindowWidth(),
                    this.getWindowHeight(),
                    this.getBorderRadius(),
                    this
            ));
        }).dimensions(
                this.getWindowLeft() + this.getWindowWidth() / 6,
                this.getWindowBottom() - 25,
                2 * this.getWindowWidth() / 3,
                20
        ).build();
        this.addDrawableChild(this.submit);
    }
}
