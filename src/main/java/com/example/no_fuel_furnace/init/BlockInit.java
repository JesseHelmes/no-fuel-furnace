package com.example.no_fuel_furnace.init;

import com.example.no_fuel_furnace.NoFuelFurnaceMod;
import com.example.no_fuel_furnace.block.NoFuelFurnaceBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, NoFuelFurnaceMod.MODID);

	public static final RegistryObject<NoFuelFurnaceBlock> NO_FUEL_FURNACE = BLOCKS.register("no_fuel_furnace",
			() -> new NoFuelFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.FURNACE)));
}
