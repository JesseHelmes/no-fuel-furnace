package com.example.no_fuel_furnace.blockentity;

import com.example.no_fuel_furnace.init.BlockEntityInit;
import com.example.no_fuel_furnace.menu.NoFuelFurnaceMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class NoFuelFurnaceBlockEntity extends AbstractNoFuelFurnaceBlockEntity {
	public NoFuelFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(BlockEntityInit.NO_FUEL_FURNACE.get(), blockPos, blockState, RecipeType.SMELTING);
	}

	protected Component getDefaultName() {
		return Component.translatable("gui.no_fuel_furnace.container.no_fuel_furnace");
	}

	protected AbstractContainerMenu createMenu(int p_59293_, Inventory inventory) {
		return new NoFuelFurnaceMenu(p_59293_, inventory, this, this.dataAccess);
	}

	public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, NoFuelFurnaceBlockEntity entity) {
		entity.updateBurnDuratation(90, true);

		AbstractNoFuelFurnaceBlockEntity.serverTick(level, blockPos, blockState, entity);
	}
}
