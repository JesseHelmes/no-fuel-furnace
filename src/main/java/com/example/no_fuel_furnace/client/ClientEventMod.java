package com.example.no_fuel_furnace.client;

import com.example.no_fuel_furnace.NoFuelFurnaceMod;
import com.example.no_fuel_furnace.client.screen.NoFuelFurnaceScreen;
import com.example.no_fuel_furnace.init.MenuInit;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = NoFuelFurnaceMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEventMod {
	public static void init(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(MenuInit.NO_FUEL_FURNACE.get(), NoFuelFurnaceScreen::new);
		});
	}
}
