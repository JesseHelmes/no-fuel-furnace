package com.example.no_fuel_furnace.init;

import com.example.no_fuel_furnace.NoFuelFurnaceMod;
import com.example.no_fuel_furnace.blockentity.NoFuelFurnaceBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
			DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, NoFuelFurnaceMod.MODID);

	public static final RegistryObject<BlockEntityType<NoFuelFurnaceBlockEntity>> NO_FUEL_FURNACE = BLOCK_ENTITIES
			.register("no_fuel_furnace",
					() -> BlockEntityType.Builder.of(NoFuelFurnaceBlockEntity::new, BlockInit.NO_FUEL_FURNACE.get())
							.build(null));
}
