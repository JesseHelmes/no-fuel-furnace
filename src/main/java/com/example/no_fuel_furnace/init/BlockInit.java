package com.example.no_fuel_furnace.init;

import com.example.no_fuel_furnace.NoFuelFurnaceMod;
import com.example.no_fuel_furnace.block.NoFuelFurnaceBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockInit {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(NoFuelFurnaceMod.MODID);

//	public static final RegistryObject<NoFuelFurnaceBlock> NO_FUEL_FURNACE = BLOCKS.register("no_fuel_furnace",
//			() -> new NoFuelFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.FURNACE)));
	
	public static final DeferredBlock<Block> NO_FUEL_FURNACE = BLOCKS.registerBlock("no_fuel_furnace", 
			properties -> new NoFuelFurnaceBlock(properties)
		);
}
