package me.jakubok.nationsmod;

import me.jakubok.nationsmod.entity.human.HumanData;
import me.jakubok.nationsmod.networking.ServerNetworking;
import me.jakubok.nationsmod.registries.BlockRegistry;
import me.jakubok.nationsmod.registries.EntityRegistry;
import me.jakubok.nationsmod.registries.ItemRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class NationsMod implements ModInitializer {

	public static final String MOD_ID = "nationsmod";
	
	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(new Identifier(MOD_ID, "item_group"))
	.icon(() -> new ItemStack(ItemRegistry.CONSTITUTION))
	.build();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("The nations mod is initialising...");
		ItemRegistry.init();
		BlockRegistry.init();
		EntityRegistry.init();
		ServerNetworking.register();
		TrackedDataHandlerRegistry.register(HumanData.HUMAN_DATA_HANDLER);
	}
}
