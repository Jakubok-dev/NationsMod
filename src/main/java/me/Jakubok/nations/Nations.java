package me.Jakubok.nations;

import me.Jakubok.nations.util.Blocks;
import me.Jakubok.nations.util.GUIs;
import me.Jakubok.nations.util.Items;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class Nations implements ModInitializer {

	public static final String MOD_ID = "nationsmod";
	public static final ItemGroup nations_tab = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "nations_tab"), () -> new ItemStack(Items.CONSTITUTION));
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("Nations mod initializing!");
		Blocks.init();
		Items.init();
		GUIs.init();
	}
}
