package me.jakubok.nationsmod.gui.miscellaneous.form;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class BasicValidations {
    public static Text BASIC_STRING_VALIDATION(Object o) {
        if (!(o instanceof String str))
            return Text.literal("ERROR, Object is not an instance of string").formatted(Formatting.RED);
        if (str.trim().equals(""))
            return Text.literal("Input is empty!").formatted(Formatting.RED);
        return Text.of("");
    }
}
