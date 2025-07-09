package com.example.no_fuel_furnace;

import com.example.no_fuel_furnace.init.BlockEntityInit;
import com.example.no_fuel_furnace.init.BlockInit;
import com.example.no_fuel_furnace.init.ItemInit;
import com.example.no_fuel_furnace.init.MenuInit;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(NoFuelFurnaceMod.MODID)
public class NoFuelFurnaceMod {
	// Define mod id in a common place for everything to reference
	public static final String MODID = "no_fuel_furnace";

	public NoFuelFurnaceMod(IEventBus modEventBus, ModContainer modContainer) {
		// Register the Deferred Register to the mod event bus so blocks get registered
		ItemInit.ITEMS.register(modEventBus);
		BlockInit.BLOCKS.register(modEventBus);
		BlockEntityInit.BLOCK_ENTITIES.register(modEventBus);
		MenuInit.MENUS.register(modEventBus);
	}
}
