package me.jakubok.nationsmod.administration.law;

import net.minecraft.text.Text;

public enum LawApprovement {
    CONSENT_NOT_NEEDED(0, Text.of("no consent needed")),
    BY_LEGISLATIVE_OR_EXECUTIVE(1, Text.of("consented by any institution")),
    LEGISLATIVE_ONLY(2, Text.of("consented by legislative")),
    EXECUTIVE_ONLY(3, Text.of("consented by executive")),
    BY_LEGISLATIVE_AND_EXECUTIVE(4, Text.of("consented by both institutions"));

    LawApprovement(int value, Text displayText) {
        this.value = value;
        this.displayText = displayText;
    }

    public final int value;
    public final Text displayText;
}
