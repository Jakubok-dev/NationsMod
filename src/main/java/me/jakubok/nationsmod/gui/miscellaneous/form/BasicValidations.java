package me.jakubok.nationsmod.gui.miscellaneous.form;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class BasicValidations {
    public static Text BASIC_STRING_VALIDATION(Object o) {
        if (!(o instanceof String str))
            return Text.literal("ERROR, Object is not an instance of string").formatted(Formatting.RED);
        if (str.trim().equals(""))
            return Text.literal("Input is empty!").formatted(Formatting.RED);
        return Text.of("");
    }

    public static Text BASIC_INTEGER_VALIDATION(Object o) {
        Text res = BasicValidations.BASIC_STRING_VALIDATION(o);
        if (!res.getString().equals(""))
            return res;
        String input = (String) o;
        Pattern pattern = Pattern.compile("^-?[1-9]\\d*$|^-?\\d$");
        if (!pattern.matcher(input).find())
            return Text.literal("Not an integer!").formatted(Formatting.RED);
        return Text.of("");
    }

    public static Text BASIC_PERCENTAGE_VALIDATION(Object o) {
        Text res = BasicValidations.BASIC_STRING_VALIDATION(o);
        if (!res.getString().equals(""))
            return res;
        String input = (String) o;
        Pattern pattern = Pattern.compile("^(-?[1-9]\\d*)%?$|^(-?\\d)%?$");
        if (!pattern.matcher(input).find())
            return Text.literal("Incorrect percentage!").formatted(Formatting.RED);
        return Text.of("");
    }
}
