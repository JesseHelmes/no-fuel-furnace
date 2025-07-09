package com.example.no_fuel_furnace.init;

import com.example.no_fuel_furnace.NoFuelFurnaceMod;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemInit {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NoFuelFurnaceMod.MODID);

//	public static final DeferredItem<BlockItem> NO_FUEL_FURNACE = ITEMS.register("no_fuel_furnace",
//			() -> new BlockItem(BlockInit.NO_FUEL_FURNACE.get(),
//					(new Item.Properties())));
	
	public static final DeferredItem<Item> NO_FUEL_FURNACE = ITEMS.registerItem("no_fuel_furnace",
			(properties) -> new BlockItem(BlockInit.NO_FUEL_FURNACE.get(), properties)
		);
}
