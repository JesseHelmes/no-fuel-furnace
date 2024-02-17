package com.example.no_fuel_furnace.init;

import com.example.no_fuel_furnace.NoFuelFurnaceMod;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
			NoFuelFurnaceMod.MODID);

	public static final RegistryObject<BlockItem> NO_FUEL_FURNACE = ITEMS.register("no_fuel_furnace",
			() -> new BlockItem(BlockInit.NO_FUEL_FURNACE.get(),
					(new Item.Properties())));
}
