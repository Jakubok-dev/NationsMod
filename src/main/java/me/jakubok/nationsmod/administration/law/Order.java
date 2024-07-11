package me.jakubok.nationsmod.administration.law;

import me.jakubok.nationsmod.administration.abstractEntities.LegalOrganisation;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;

public record Order(
        Text displayName,
        Text description,
        GetOnTriggerMessage getOnTriggerMessage,
        OnTrigger onTrigger
) {
    @FunctionalInterface
    public interface GetOnTriggerMessage {
        List<OrderedText> get(NbtCompound context, LegalOrganisation<?> organisation, TextRenderer renderer, int width);
    }

    @FunctionalInterface
    public interface OnTrigger {
        void trigger(NbtCompound context, LegalOrganisation<?> organisation, MinecraftServer server);
    }
}