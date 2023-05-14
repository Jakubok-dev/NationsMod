package me.jakubok.nationsmod;

import me.jakubok.nationsmod.networking.ServerNetworking;
import me.jakubok.nationsmod.registries.BlockRegistry;
import me.jakubok.nationsmod.registries.EntityRegistry;
import me.jakubok.nationsmod.registries.ItemRegistry;
import me.jakubok.nationsmod.registries.StatusEffectRegistry;
import me.jakubok.nationsmod.registries.ModsTrackedDataHandlerRegistry;
import net.fabricmc.api.ModInitializer;

public class NationsMod implements ModInitializer {

	public static final String MOD_ID = "nationsmod";

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("The nations mod is initialising...");
		ItemRegistry.init();
		BlockRegistry.init();
		EntityRegistry.init();
		StatusEffectRegistry.init();
		ServerNetworking.register();
		ModsTrackedDataHandlerRegistry.init();
	}
}
