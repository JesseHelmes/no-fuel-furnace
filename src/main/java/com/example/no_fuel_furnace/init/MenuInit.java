package com.example.no_fuel_furnace.init;

import com.example.no_fuel_furnace.NoFuelFurnaceMod;
import com.example.no_fuel_furnace.menu.NoFuelFurnaceMenu;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuInit {
	public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, NoFuelFurnaceMod.MODID);

	public static final RegistryObject<MenuType<NoFuelFurnaceMenu>> NO_FUEL_FURNACE = MENUS.register("no_fuel_furnace_menu",
			(() -> IForgeMenuType.create((pWindowID, pInventory, pData) -> {
				return new NoFuelFurnaceMenu(pWindowID, pInventory);
			})));
}