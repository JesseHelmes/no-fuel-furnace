package com.example.no_fuel_furnace.block;

import javax.annotation.Nullable;

import com.example.no_fuel_furnace.blockentity.NoFuelFurnaceBlockEntity;
import com.example.no_fuel_furnace.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class NoFuelFurnaceBlock extends AbstractNoFuelFurnaceBlock {
	public NoFuelFurnaceBlock(Properties properties) {
		super(properties);
	}

	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new NoFuelFurnaceBlockEntity(blockPos, blockState);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
		return createNoFuelFurnaceTicker(level, blockEntityType, BlockEntityInit.NO_FUEL_FURNACE.get());
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> createNoFuelFurnaceTicker(Level level, BlockEntityType<T> blockEntityType, BlockEntityType<? extends NoFuelFurnaceBlockEntity> blockEntity) {
		return !level.isClientSide ? createTickerHelper(blockEntityType, blockEntity, NoFuelFurnaceBlockEntity::serverTick) : null;
	}

	protected void openContainer(Level level, BlockPos blockPos, Player player) {
		BlockEntity blockentity = level.getBlockEntity(blockPos);
		if (blockentity instanceof NoFuelFurnaceBlockEntity) {
			player.openMenu((MenuProvider)blockentity);
			player.awardStat(Stats.INTERACT_WITH_FURNACE);
		}
	}

	public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource source) {
		if (blockState.getValue(LIT)) {
			double d0 = (double)blockPos.getX() + 0.5D;
			double d1 = (double)blockPos.getY();
			double d2 = (double)blockPos.getZ() + 0.5D;
			if (source.nextDouble() < 0.1D) {
				level.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
			}

			Direction direction = blockState.getValue(FACING);
			Direction.Axis direction$axis = direction.getAxis();
			double d3 = 0.52D;
			double d4 = source.nextDouble() * 0.6D - 0.3D;
			double d5 = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * d3 : d4;
			double d6 = source.nextDouble() * 6.0D / 16.0D;
			double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * d3 : d4;
			level.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
			level.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
		}
	}
}
