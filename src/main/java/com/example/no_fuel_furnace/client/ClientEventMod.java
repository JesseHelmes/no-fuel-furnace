package com.example.no_fuel_furnace.client;

import com.example.no_fuel_furnace.NoFuelFurnaceMod;
import com.example.no_fuel_furnace.client.screen.NoFuelFurnaceScreen;
import com.example.no_fuel_furnace.init.MenuInit;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = NoFuelFurnaceMod.MODID, value = Dist.CLIENT)
public class ClientEventMod {
	@SubscribeEvent
	public static void registerScreens(RegisterMenuScreensEvent event) {
		event.register(MenuInit.NO_FUEL_FURNACE.get(), NoFuelFurnaceScreen::new);
	}
}
