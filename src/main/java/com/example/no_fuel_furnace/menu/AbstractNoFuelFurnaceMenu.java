package com.example.no_fuel_furnace.menu;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

//in order to convert it and make it work, i had to add the INGREDIENT_SLOT, FUEL_SLOT, RESULT_SLOT, SLOT_COUNT, INV_SLOT_START
//INV_SLOT_END, USE_ROW_SLOT_START, USE_ROW_SLOT_END
//in the right places and adjust the other static slots and SLOT_COUNT
// copy of AbstractFurnaceMenu
public abstract class AbstractNoFuelFurnaceMenu extends RecipeBookMenu<Container> {
	public static final int INGREDIENT_SLOT = 0;
//	public static final int FUEL_SLOT = 1;
	public static final int RESULT_SLOT = 1;
	public static final int SLOT_COUNT = 2;
	public static final int DATA_COUNT = 4;
	private static final int INV_SLOT_START = 2;
	private static final int INV_SLOT_END = 29;
	private static final int USE_ROW_SLOT_START = 29;
	private static final int USE_ROW_SLOT_END = 38;
	private final Container container;
	private final ContainerData data;
	protected final Level level;
	private final RecipeType<? extends AbstractCookingRecipe> recipeType;
	private final RecipeBookType recipeBookType;

	protected AbstractNoFuelFurnaceMenu(MenuType<?> menuType, RecipeType<? extends AbstractCookingRecipe> recipeType, RecipeBookType recipeBookType, int p_38963_, Inventory inventory) {
		this(menuType, recipeType, recipeBookType, p_38963_, inventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(DATA_COUNT));
	}

	protected AbstractNoFuelFurnaceMenu(MenuType<?> menuType, RecipeType<? extends AbstractCookingRecipe> recipeType, RecipeBookType recipeBookType, int p_38969_, Inventory inventory, Container container, ContainerData containerData) {
		super(menuType, p_38969_);
		this.recipeType = recipeType;
		this.recipeBookType = recipeBookType;
		checkContainerSize(container, SLOT_COUNT);
		checkContainerDataCount(containerData, DATA_COUNT);
		this.container = container;
		this.data = containerData;
		this.level = inventory.player.level();
		int ingredientOffsetFromOrignal = 17;
		this.addSlot(new Slot(container, INGREDIENT_SLOT, 56, 17 + ingredientOffsetFromOrignal)); //was 0, 56, 17
//		this.addSlot(new FurnaceFuelSlot(this, container, FUEL_SLOT, 56, 53));
		this.addSlot(new FurnaceResultSlot(inventory.player, container, RESULT_SLOT, 116, 35));

		// player inventory
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		// player inventory hotbar
		for(int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
		}

		this.addDataSlots(containerData);
	}

	public void fillCraftSlotsStackedContents(StackedContents stackedContents) {
		if (this.container instanceof StackedContentsCompatible) {
			((StackedContentsCompatible)this.container).fillStackedContents(stackedContents);
		}

	}

	public void clearCraftingContent() {
		this.getSlot(INGREDIENT_SLOT).set(ItemStack.EMPTY);
		this.getSlot(RESULT_SLOT).set(ItemStack.EMPTY);
	}

	public boolean recipeMatches(Recipe<? super Container> recipe) {
		return recipe.matches(this.container, this.level);
	}

	public int getResultSlotIndex() {
		return RESULT_SLOT;
	}

	public int getGridWidth() {
		return 1;
	}

	public int getGridHeight() {
		return 1;
	}

	public int getSize() {
		return SLOT_COUNT;
	}

	public boolean stillValid(Player player) {
		return this.container.stillValid(player);
	}

	public ItemStack quickMoveStack(Player player, int slotIndex) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotIndex);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (slotIndex == RESULT_SLOT) {
				if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (slotIndex != INGREDIENT_SLOT) { // slotIndex != FUEL_SLOT &&
				if (this.canSmelt(itemstack1)) {
					if (!this.moveItemStackTo(itemstack1, INGREDIENT_SLOT, RESULT_SLOT, false)) {// FUEL_SLOT, false
						return ItemStack.EMPTY;
					}
				// crashes if i placed a fuel item in and pulled it out without this check
				} 
//				else if (this.isFuel(itemstack1)) {
//					if (!this.moveItemStackTo(itemstack1, FUEL_SLOT, RESULT_SLOT, false)) {
//						return ItemStack.EMPTY;
//					}
//				} 
				else if (slotIndex >= INV_SLOT_START && slotIndex < INV_SLOT_END) {
					if (!this.moveItemStackTo(itemstack1, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
						return ItemStack.EMPTY;
					}
				} else if (slotIndex >= USE_ROW_SLOT_START && slotIndex < USE_ROW_SLOT_END && !this.moveItemStackTo(itemstack1, INV_SLOT_START, INV_SLOT_END, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}

	protected boolean canSmelt(ItemStack p_38978_) {
		return this.level.getRecipeManager().getRecipeFor((RecipeType<AbstractCookingRecipe>)this.recipeType, new SimpleContainer(p_38978_), this.level).isPresent();
	}

	protected boolean isFuel(ItemStack p_38989_) {
		return net.minecraftforge.common.ForgeHooks.getBurnTime(p_38989_, this.recipeType) > 0;
	}

	public int getBurnProgress() {
		int i = this.data.get(2);
		int j = this.data.get(3);
		return j != 0 && i != 0 ? i * 24 / j : 0;
	}

	public int getLitProgress() {
		int i = this.data.get(1);
		if (i == 0) {
			i = 200;
		}

		return this.data.get(0) * 13 / i;
	}

	public boolean isLit() {
		return this.data.get(0) > 0;
	}

	public RecipeBookType getRecipeBookType() {
		return this.recipeBookType;
	}

	public boolean shouldMoveToInventory(int p_150463_) {
		return p_150463_ != 1;
	}
}