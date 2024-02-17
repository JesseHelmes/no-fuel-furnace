package com.example.no_fuel_furnace.blockentity;

import java.util.List;

import javax.annotation.Nullable;

import com.example.no_fuel_furnace.block.AbstractNoFuelFurnaceBlock;
import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

// in order to convert it and make it work, i had to add the SLOT_INPUT, SLOT_FUEL, SLOT_FUEL
// in the right places
// copy of AbstractFurnaceBlockEntity
public abstract class AbstractNoFuelFurnaceBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeHolder, StackedContentsCompatible {
	protected static final int SLOT_INPUT = 0;
//	protected static final int SLOT_FUEL = 1;
	protected static final int SLOT_RESULT = 1;
	public static final int DATA_LIT_TIME = 0;
	private static final int[] SLOTS_FOR_UP = new int[]{SLOT_INPUT};
	private static final int[] SLOTS_FOR_DOWN = new int[]{SLOT_RESULT};// SLOT_RESULT, SLOT_FUEL;
	private static final int[] SLOTS_FOR_SIDES = new int[]{SLOT_INPUT}; // SLOT_FUEL
	public static final int DATA_LIT_DURATION = 1;
	public static final int DATA_COOKING_PROGRESS = 2;
	public static final int DATA_COOKING_TOTAL_TIME = 3;
	public static final int NUM_DATA_VALUES = 4;
	public static final int BURN_TIME_STANDARD = 200;
	public static final int BURN_COOL_SPEED = 2;
	private final RecipeType<? extends AbstractCookingRecipe> recipeType;
	protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
	int litTime;
	int litDuration;
	int cookingProgress;
	int cookingTotalTime;
	int burnDuration;
	protected final ContainerData dataAccess = new ContainerData() {
		public int get(int p_58431_) {
			switch (p_58431_) {
				case 0:
					return AbstractNoFuelFurnaceBlockEntity.this.litTime;
				case 1:
					return AbstractNoFuelFurnaceBlockEntity.this.litDuration;
				case 2:
					return AbstractNoFuelFurnaceBlockEntity.this.cookingProgress;
				case 3:
					return AbstractNoFuelFurnaceBlockEntity.this.cookingTotalTime;
				default:
					return 0;
			}
		}

		public void set(int p_58433_, int p_58434_) {
			switch (p_58433_) {
				case 0:
					AbstractNoFuelFurnaceBlockEntity.this.litTime = p_58434_;
					break;
				case 1:
					AbstractNoFuelFurnaceBlockEntity.this.litDuration = p_58434_;
					break;
				case 2:
					AbstractNoFuelFurnaceBlockEntity.this.cookingProgress = p_58434_;
					break;
				case 3:
					AbstractNoFuelFurnaceBlockEntity.this.cookingTotalTime = p_58434_;
			}

		}

		public int getCount() {
			return 4;
		}
	};
	private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
	private final RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> quickCheck;

	protected AbstractNoFuelFurnaceBlockEntity(BlockEntityType<?> p_154991_, BlockPos p_154992_, BlockState p_154993_, RecipeType<? extends AbstractCookingRecipe> p_154994_) {
		super(p_154991_, p_154992_, p_154993_);
		this.quickCheck = RecipeManager.createCheck((RecipeType)p_154994_);
		this.recipeType = p_154994_;
	}

	private boolean isLit() {
		return this.litTime > 0;
	}

	public void load(CompoundTag compoundTag) {
		super.load(compoundTag);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(compoundTag, this.items);
		this.litTime = compoundTag.getInt("BurnTime");
		this.cookingProgress = compoundTag.getInt("CookTime");
		this.cookingTotalTime = compoundTag.getInt("CookTimeTotal");
		this.litDuration = compoundTag.getInt("litDuration"); // this.getBurnDuration(this.items.get(SLOT_FUEL));
		CompoundTag compoundtag = compoundTag.getCompound("RecipesUsed");

		for(String s : compoundtag.getAllKeys()) {
			this.recipesUsed.put(new ResourceLocation(s), compoundtag.getInt(s));
		}

	}

	protected void saveAdditional(CompoundTag compoundTag) {
		super.saveAdditional(compoundTag);
		compoundTag.putInt("BurnTime", this.litTime);
		compoundTag.putInt("litDuration", this.litDuration);
		compoundTag.putInt("CookTime", this.cookingProgress);
		compoundTag.putInt("CookTimeTotal", this.cookingTotalTime);
		ContainerHelper.saveAllItems(compoundTag, this.items);
		CompoundTag compoundtag = new CompoundTag();
		this.recipesUsed.forEach((p_187449_, p_187450_) -> {
			compoundtag.putInt(p_187449_.toString(), p_187450_);
		});
		compoundTag.put("RecipesUsed", compoundtag);
	}

	public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, AbstractNoFuelFurnaceBlockEntity blockEntity) {
		boolean flag = blockEntity.isLit();
		boolean flag1 = false;
		if (blockEntity.isLit()) {
			--blockEntity.litTime;
		}

//		ItemStack itemstack = blockEntity.items.get(SLOT_FUEL);
		boolean flag2 = !blockEntity.items.get(SLOT_INPUT).isEmpty();
//		boolean flag3 = !itemstack.isEmpty();
		if (blockEntity.isLit() || flag2) {// flag3 && flag2
			Recipe<?> recipe;
			if (flag2) {
				recipe = blockEntity.quickCheck.getRecipeFor(blockEntity, level).orElse(null);
			} else {
				recipe = null;
			}

			int i = blockEntity.getMaxStackSize();
			if (!blockEntity.isLit() && blockEntity.canBurn(level.registryAccess(), recipe, blockEntity.items, i)) {
//				blockEntity.litTime = blockEntity.getBurnDuration(itemstack);
				blockEntity.litTime = blockEntity.burnDuration;
				blockEntity.litDuration = blockEntity.litTime;
				if (blockEntity.isLit()) {
					flag1 = true;
//					if (itemstack.hasCraftingRemainingItem())
//						blockEntity.items.set(SLOT_FUEL, itemstack.getCraftingRemainingItem());
//					else
//					if (flag3) {
//						Item item = itemstack.getItem();
//						itemstack.shrink(1);
//						if (itemstack.isEmpty()) {
//							blockEntity.items.set(SLOT_FUEL, itemstack.getCraftingRemainingItem());
//						}
//					}
				}
			}

			if (blockEntity.isLit() && blockEntity.canBurn(level.registryAccess(), recipe, blockEntity.items, i)) {
				++blockEntity.cookingProgress;
				if (blockEntity.cookingProgress == blockEntity.cookingTotalTime) {
					blockEntity.cookingProgress = 0;
					blockEntity.cookingTotalTime = getTotalCookTime(level, blockEntity);
					if (blockEntity.burn(level.registryAccess(), recipe, blockEntity.items, i)) {
						blockEntity.setRecipeUsed(recipe);
					}

					flag1 = true;
				}
			} else {
				blockEntity.cookingProgress = 0;
			}
		} else if (!blockEntity.isLit() && blockEntity.cookingProgress > 0) {
			blockEntity.cookingProgress = Mth.clamp(blockEntity.cookingProgress - 2, 0, blockEntity.cookingTotalTime);
		}

		if (flag != blockEntity.isLit()) {
			flag1 = true;
			blockState = blockState.setValue(AbstractNoFuelFurnaceBlock.LIT, Boolean.valueOf(blockEntity.isLit()));
			level.setBlock(blockPos, blockState, 3);
		}

		if (flag1) {
			setChanged(level, blockPos, blockState);
		}

	}

	private boolean canBurn(RegistryAccess p_266924_, @Nullable Recipe<?> recipe, NonNullList<ItemStack> items, int p_155008_) {
		if (!items.get(SLOT_INPUT).isEmpty() && recipe != null) {
			ItemStack itemstack = ((Recipe<WorldlyContainer>) recipe).assemble(this, p_266924_);
			if (itemstack.isEmpty()) {
				return false;
			} else {
				ItemStack itemstack1 = items.get(SLOT_RESULT);
				if (itemstack1.isEmpty()) {
					return true;
				} else if (!ItemStack.isSameItem(itemstack1, itemstack)) {
					return false;
				} else if (itemstack1.getCount() + itemstack.getCount() <= p_155008_ && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
					return true;
				} else {
					return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
				}
			}
		} else {
			return false;
		}
	}

	private boolean burn(RegistryAccess p_266740_, @Nullable Recipe<?> p_266780_, NonNullList<ItemStack> items, int p_267157_) {
		if (p_266780_ != null && this.canBurn(p_266740_, p_266780_, items, p_267157_)) {
			ItemStack itemstack = items.get(SLOT_INPUT);
			ItemStack itemstack1 = ((Recipe<WorldlyContainer>) p_266780_).assemble(this, p_266740_);
			ItemStack itemstack2 = items.get(SLOT_RESULT);
			if (itemstack2.isEmpty()) {
				items.set(SLOT_RESULT, itemstack1.copy());
			} else if (itemstack2.is(itemstack1.getItem())) {
				itemstack2.grow(itemstack1.getCount());
			}

//			if (itemstack.is(Blocks.WET_SPONGE.asItem()) && !items.get(SLOT_FUEL).isEmpty() && items.get(SLOT_FUEL).is(Items.BUCKET)) {
//				items.set(SLOT_FUEL, new ItemStack(Items.WATER_BUCKET));
//			}

			itemstack.shrink(1);
			return true;
		} else {
			return false;
		}
	}

	protected int getBurnDuration(ItemStack itemstack) {
		if (itemstack.isEmpty()) {
			return 0;
		} else {
//			Item item = itemstack.getItem();
			return net.minecraftforge.common.ForgeHooks.getBurnTime(itemstack, this.recipeType);
		}
	}

	private static int getTotalCookTime(Level level, AbstractNoFuelFurnaceBlockEntity blockEntity) {
		return blockEntity.quickCheck.getRecipeFor(blockEntity, level).map(AbstractCookingRecipe::getCookingTime).orElse(BURN_TIME_STANDARD);
	}

	public static boolean isFuel(ItemStack itemstack) {
		return net.minecraftforge.common.ForgeHooks.getBurnTime(itemstack, null) > 0;
	}

	public int[] getSlotsForFace(Direction direction) {
		if (direction == Direction.DOWN) {
			return SLOTS_FOR_DOWN;
		} else {
			return direction == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
		}
	}

	public boolean canPlaceItemThroughFace(int p_58336_, ItemStack itemstack, @Nullable Direction direction) {
		return this.canPlaceItem(p_58336_, itemstack);
	}

	public boolean canTakeItemThroughFace(int p_58392_, ItemStack itemstack, Direction direction) {
		return true;
//		if (direction == Direction.DOWN && p_58392_ == SLOT_FUEL) {
//			return itemstack.is(Items.WATER_BUCKET) || itemstack.is(Items.BUCKET);
//		} else {
//			return true;
//		}
	}

	public int getContainerSize() {
		return this.items.size();
	}

	public boolean isEmpty() {
		for(ItemStack itemstack : this.items) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	public ItemStack getItem(int p_58328_) {
		return this.items.get(p_58328_);
	}

	public ItemStack removeItem(int p_58330_, int p_58331_) {
		return ContainerHelper.removeItem(this.items, p_58330_, p_58331_);
	}

	public ItemStack removeItemNoUpdate(int p_58387_) {
		return ContainerHelper.takeItem(this.items, p_58387_);
	}

	public void setItem(int p_58333_, ItemStack itemstackIn) {
		ItemStack itemstack = this.items.get(p_58333_);
		boolean flag = !itemstackIn.isEmpty() && ItemStack.isSameItemSameTags(itemstack, itemstackIn);
		this.items.set(p_58333_, itemstackIn);
		if (itemstackIn.getCount() > this.getMaxStackSize()) {
			itemstackIn.setCount(this.getMaxStackSize());
		}

		if (p_58333_ == 0 && !flag) {
			this.cookingTotalTime = getTotalCookTime(this.level, this);
			this.cookingProgress = 0;
			this.setChanged();
		}

	}

	public boolean stillValid(Player player) {
		return Container.stillValidBlockEntity(this, player);
	}

	public boolean canPlaceItem(int p_58389_, ItemStack itemstackIn) {
		if (p_58389_ == SLOT_RESULT) {
			return false;
		} else {
			return true;
		}
//		} else if (p_58389_ != 1) {
//			return true;
//		} else {
//			ItemStack itemstack = this.items.get(SLOT_FUEL);
//			return net.minecraftforge.common.ForgeHooks.getBurnTime(itemstackIn, this.recipeType) > 0 || itemstackIn.is(Items.BUCKET) && !itemstack.is(Items.BUCKET);
//		}
	}

	public void clearContent() {
		this.items.clear();
	}

	public void setRecipeUsed(@Nullable Recipe<?> recipe) {
		if (recipe != null) {
			ResourceLocation resourcelocation = recipe.getId();
			this.recipesUsed.addTo(resourcelocation, 1);
		}

	}

	@Nullable
	public Recipe<?> getRecipeUsed() {
		return null;
	}

	public void awardUsedRecipes(Player player, List<ItemStack> p_282202_) {
	}

	public void awardUsedRecipesAndPopExperience(ServerPlayer serverPlayer) {
		List<Recipe<?>> list = this.getRecipesToAwardAndPopExperience(serverPlayer.serverLevel(), serverPlayer.position());
		serverPlayer.awardRecipes(list);

		for(Recipe<?> recipe : list) {
			if (recipe != null) {
				serverPlayer.triggerRecipeCrafted(recipe, this.items);
			}
		}

		this.recipesUsed.clear();
	}

	public List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel serverLevel, Vec3 vec3) {
		List<Recipe<?>> list = Lists.newArrayList();

		for(Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
			serverLevel.getRecipeManager().byKey(entry.getKey()).ifPresent((p_155023_) -> {
				list.add(p_155023_);
				createExperience(serverLevel, vec3, entry.getIntValue(), ((AbstractCookingRecipe)p_155023_).getExperience());
			});
		}

		return list;
	}

	private static void createExperience(ServerLevel serverLevel, Vec3 vec3, int p_155001_, float p_155002_) {
		int i = Mth.floor((float)p_155001_ * p_155002_);
		float f = Mth.frac((float)p_155001_ * p_155002_);
		if (f != 0.0F && Math.random() < (double)f) {
			++i;
		}

		ExperienceOrb.award(serverLevel, vec3, i);
	}

	public void fillStackedContents(StackedContents p_58342_) {
		for(ItemStack itemstack : this.items) {
			p_58342_.accountStack(itemstack);
		}

	}

	net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
			net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

	@Override
	public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
		if (!this.remove && facing != null && capability == net.minecraftforge.common.capabilities.ForgeCapabilities.ITEM_HANDLER) {
			if (facing == Direction.UP)
				return handlers[0].cast();
			else if (facing == Direction.DOWN)
				return handlers[1].cast();
			else
				return handlers[2].cast();
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		for (int x = 0; x < handlers.length; x++)
			handlers[x].invalidate();
	}

	@Override
	public void reviveCaps() {
		super.reviveCaps();
		this.handlers = net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
	}

	public void updateBurnDuratation(int burnDuration, boolean shouldUpdate) {
		if(shouldUpdate) {
			ItemStack itemstack = this.items.get(SLOT_INPUT);
			if(!itemstack.isEmpty()) {
				this.burnDuration = burnDuration;
				if(this.isLit()) {
					this.litTime = this.burnDuration;
				}
			}
		} else {
			this.burnDuration = 0;
		}
	}
}
