package com.example.no_fuel_furnace.init;

import com.example.no_fuel_furnace.NoFuelFurnaceMod;
import com.example.no_fuel_furnace.menu.NoFuelFurnaceMenu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MenuInit {
	public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, NoFuelFurnaceMod.MODID);

//	public static final RegistryObject<MenuType<NoFuelFurnaceMenu>> NO_FUEL_FURNACE = MENUS.register("no_fuel_furnace_menu",
//			(() -> IForgeMenuType.create((pWindowID, pInventory, pData) -> {
//				return new NoFuelFurnaceMenu(pWindowID, pInventory);
//			})));
	
//	public static final Supplier<MenuType<NoFuelFurnaceMenu>> NO_FUEL_FURNACE = MENUS.register("no_fuel_furnace_menu",
//			() -> IMenuTypeExtension.create(NoFuelFurnaceMenu::new));

	public static final DeferredHolder<MenuType<?>, MenuType<NoFuelFurnaceMenu>> NO_FUEL_FURNACE = MENUS.register("no_fuel_furnace_menu",
			() -> new MenuType<>(NoFuelFurnaceMenu::new, FeatureFlags.DEFAULT_FLAGS));

}