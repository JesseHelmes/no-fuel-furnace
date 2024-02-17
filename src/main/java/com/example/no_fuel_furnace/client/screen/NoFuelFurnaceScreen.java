package com.example.no_fuel_furnace.client.screen;

import com.example.no_fuel_furnace.NoFuelFurnaceMod;
import com.example.no_fuel_furnace.menu.AbstractNoFuelFurnaceMenu;

import net.minecraft.client.gui.screens.recipebook.SmeltingRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NoFuelFurnaceScreen extends AbstractNoFuelFurnaceScreen<AbstractNoFuelFurnaceMenu> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(NoFuelFurnaceMod.MODID, "textures/gui/no_fuel_furnace.png");

	public NoFuelFurnaceScreen(AbstractNoFuelFurnaceMenu menu, Inventory inventory, Component component) {
		super(menu, new SmeltingRecipeBookComponent(), inventory, component, TEXTURE);
	}
}