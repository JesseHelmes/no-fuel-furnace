package com.example.no_fuel_furnace.block;

import javax.annotation.Nullable;

import com.example.no_fuel_furnace.blockentity.AbstractNoFuelFurnaceBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

// copy of AbstractFurnaceBlock
public abstract class AbstractNoFuelFurnaceBlock extends BaseEntityBlock {
   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
   public static final BooleanProperty LIT = BlockStateProperties.LIT;

   protected AbstractNoFuelFurnaceBlock(BlockBehaviour.Properties p_48687_) {
      super(p_48687_);
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
   }

   public InteractionResult use(BlockState p_48706_, Level p_48707_, BlockPos p_48708_, Player p_48709_, InteractionHand p_48710_, BlockHitResult p_48711_) {
      if (p_48707_.isClientSide) {
         return InteractionResult.SUCCESS;
      } else {
         this.openContainer(p_48707_, p_48708_, p_48709_);
         return InteractionResult.CONSUME;
      }
   }

   protected abstract void openContainer(Level p_48690_, BlockPos p_48691_, Player p_48692_);

   public BlockState getStateForPlacement(BlockPlaceContext p_48689_) {
      return this.defaultBlockState().setValue(FACING, p_48689_.getHorizontalDirection().getOpposite());
   }

   public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity entity, ItemStack itemstack) {
      if (itemstack.hasCustomHoverName()) {
         BlockEntity blockentity = level.getBlockEntity(blockPos);
         if (blockentity instanceof AbstractNoFuelFurnaceBlockEntity) {
            ((AbstractNoFuelFurnaceBlockEntity)blockentity).setCustomName(itemstack.getHoverName());
         }
      }

   }

   public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean p_48717_) {
      if (!blockState.is(blockState2.getBlock())) {
         BlockEntity blockentity = level.getBlockEntity(blockPos);
         if (blockentity instanceof AbstractNoFuelFurnaceBlockEntity) {
            if (level instanceof ServerLevel) {
               Containers.dropContents(level, blockPos, (AbstractNoFuelFurnaceBlockEntity)blockentity);
               ((AbstractNoFuelFurnaceBlockEntity)blockentity).getRecipesToAwardAndPopExperience((ServerLevel)level, Vec3.atCenterOf(blockPos));
            }

            level.updateNeighbourForOutputSignal(blockPos, this);
         }

         super.onRemove(blockState, level, blockPos, blockState2, p_48717_);
      }
   }

   public boolean hasAnalogOutputSignal(BlockState p_48700_) {
      return true;
   }

   public int getAnalogOutputSignal(BlockState p_48702_, Level p_48703_, BlockPos p_48704_) {
      return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(p_48703_.getBlockEntity(p_48704_));
   }

   public RenderShape getRenderShape(BlockState p_48727_) {
      return RenderShape.MODEL;
   }

   public BlockState rotate(BlockState p_48722_, Rotation p_48723_) {
      return p_48722_.setValue(FACING, p_48723_.rotate(p_48722_.getValue(FACING)));
   }

   public BlockState mirror(BlockState p_48719_, Mirror p_48720_) {
      return p_48719_.rotate(p_48720_.getRotation(p_48719_.getValue(FACING)));
   }

   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_48725_) {
      p_48725_.add(FACING, LIT);
   }

   @Nullable
   protected static <T extends BlockEntity> BlockEntityTicker<T> createFurnaceTicker(Level p_151988_, BlockEntityType<T> p_151989_, BlockEntityType<? extends AbstractNoFuelFurnaceBlockEntity> p_151990_) {
      return p_151988_.isClientSide ? null : createTickerHelper(p_151989_, p_151990_, AbstractNoFuelFurnaceBlockEntity::serverTick);
   }
}