package com.example.no_fuel_furnace.menu;

import com.example.no_fuel_furnace.init.MenuInit;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.item.crafting.RecipeType;

public class NoFuelFurnaceMenu extends AbstractNoFuelFurnaceMenu {
	public NoFuelFurnaceMenu(int containerId, Inventory playerInventory) {
		super(MenuInit.NO_FUEL_FURNACE.get(), RecipeType.SMELTING, RecipePropertySet.FURNACE_INPUT, RecipeBookType.FURNACE, containerId, playerInventory);
	}

//	
//	public NoFuelFurnaceMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf extraData) {
//		super(MenuInit.NO_FUEL_FURNACE.get(), RecipeType.SMELTING, RecipePropertySet.FURNACE_INPUT, RecipeBookType.FURNACE, containerId, playerInventory);
//	}

	public NoFuelFurnaceMenu(int containerId, Inventory playerInventory, Container furnaceContainer, ContainerData furnaceData) {
		super(MenuInit.NO_FUEL_FURNACE.get(), RecipeType.SMELTING, RecipePropertySet.FURNACE_INPUT, RecipeBookType.FURNACE, containerId, playerInventory, furnaceContainer, furnaceData);
	}
}