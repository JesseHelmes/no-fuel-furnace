package com.example.no_fuel_furnace.init;

import com.example.no_fuel_furnace.NoFuelFurnaceMod;
import com.example.no_fuel_furnace.blockentity.NoFuelFurnaceBlockEntity;
import com.google.common.base.Supplier;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockEntityInit {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
			DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, NoFuelFurnaceMod.MODID);

//	public static final RegistryObject<BlockEntityType<NoFuelFurnaceBlockEntity>> NO_FUEL_FURNACE = BLOCK_ENTITIES
//			.register("no_fuel_furnace",
//					() -> BlockEntityType.Builder.of(NoFuelFurnaceBlockEntity::new, BlockInit.NO_FUEL_FURNACE.get())
//							.build(null));

//	public static final Supplier<BlockEntityType<NoFuelFurnaceBlockEntity>> NO_FUEL_FURNACE = BLOCK_ENTITIES
//			.register("no_fuel_furnace",
//					() -> new BlockEntityType<>(
//					NoFuelFurnaceBlockEntity::new,
//					false,
//					BlockInit.NO_FUEL_FURNACE.get()
//					)
//	);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NoFuelFurnaceBlockEntity>> NO_FUEL_FURNACE = BLOCK_ENTITIES
			.register("no_fuel_furnace",
					() -> new BlockEntityType<>(
					NoFuelFurnaceBlockEntity::new,
					false,
					BlockInit.NO_FUEL_FURNACE.get()
					)
	);
}
