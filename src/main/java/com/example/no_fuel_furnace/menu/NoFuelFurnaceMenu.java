package com.example.no_fuel_furnace.menu;

import com.example.no_fuel_furnace.init.MenuInit;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.RecipeType;

public class NoFuelFurnaceMenu extends AbstractNoFuelFurnaceMenu {
	public NoFuelFurnaceMenu(int p_39532_, Inventory inventory) {
		super(MenuInit.NO_FUEL_FURNACE.get(), RecipeType.SMELTING, RecipeBookType.FURNACE, p_39532_, inventory);
	}

	public NoFuelFurnaceMenu(int p_39535_, Inventory inventory, Container container, ContainerData containerData) {
		super(MenuInit.NO_FUEL_FURNACE.get(), RecipeType.SMELTING, RecipeBookType.FURNACE, p_39535_, inventory, container, containerData);
	}
}