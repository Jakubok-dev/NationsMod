package me.jakubok.nationsmod.registries;

import me.jakubok.nationsmod.gui.MainScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class KeyBindingRegistry {
    public static final KeyBinding OPEN_MAIN_SCREEN_KEY_BINDING; 

    static {

        OPEN_MAIN_SCREEN_KEY_BINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "keybinding.nationsmod.open_main_screen", 
            InputUtil.Type.KEYSYM, 
            InputUtil.GLFW_KEY_N, 
            "keybindingcategory.nationsmod"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (OPEN_MAIN_SCREEN_KEY_BINDING.isPressed()) {
                client.setScreen(new MainScreen());
            }
        });
    }

    public static void init() {}
}
