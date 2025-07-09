package com.example.no_fuel_furnace.menu;

import java.util.List;

import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

//in order to convert it and make it work, i had to add the INGREDIENT_SLOT, FUEL_SLOT, RESULT_SLOT, SLOT_COUNT, INV_SLOT_START
//INV_SLOT_END, USE_ROW_SLOT_START, USE_ROW_SLOT_END
//in the right places and adjust the other static slots and SLOT_COUNT
// copy of AbstractFurnaceMenu
public abstract class AbstractNoFuelFurnaceMenu extends RecipeBookMenu {
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
	private final RecipePropertySet acceptedInputs;
	private final RecipeBookType recipeBookType;

    protected AbstractNoFuelFurnaceMenu(
        MenuType<?> menuType,
        RecipeType<? extends AbstractCookingRecipe> recipeType,
        ResourceKey<RecipePropertySet> acceptedInputs,
        RecipeBookType recipeBookType,
        int containerId,
        Inventory inventory
    ) {
        this(menuType, recipeType, acceptedInputs, recipeBookType, containerId, inventory, new SimpleContainer(3), new SimpleContainerData(4));
    }

	protected AbstractNoFuelFurnaceMenu(
        MenuType<?> menuType,
        RecipeType<? extends AbstractCookingRecipe> recipeType,
        ResourceKey<RecipePropertySet> acceptedInputs,
        RecipeBookType recipeBookType,
        int containerId,
        Inventory inventory,
        Container container,
        ContainerData data
	) {
		super(menuType, containerId);
		this.recipeType = recipeType;
		this.recipeBookType = recipeBookType;
		checkContainerSize(container, SLOT_COUNT);
		checkContainerDataCount(data, DATA_COUNT);
		this.container = container;
		this.data = data;
		this.level = inventory.player.level();
		this.acceptedInputs = this.level.recipeAccess().propertySet(acceptedInputs);

		// Offset to move the input from minecraft orignal place
		int ingredientOffsetFromOrignal = 17;
		this.addSlot(new Slot(container, INGREDIENT_SLOT, 56, 17 + ingredientOffsetFromOrignal)); //was 0, 56, 17
//		this.addSlot(new FurnaceFuelSlot(this, container, FUEL_SLOT, 56, 53));
		this.addSlot(new FurnaceResultSlot(inventory.player, container, RESULT_SLOT, 116, 35));

//		// player inventory
//		for(int i = 0; i < 3; ++i) {
//			for(int j = 0; j < 9; ++j) {
//				this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
//			}
//		}
//
//		// player inventory hotbar
//		for(int k = 0; k < 9; ++k) {
//			this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
//		}

		this.addStandardInventorySlots(inventory, 8, 84);

		this.addDataSlots(data);
	}

    @Override
    public void fillCraftSlotsStackedContents(StackedItemContents stackedItemContents) {
        if (this.container instanceof StackedContentsCompatible) {
            ((StackedContentsCompatible)this.container).fillStackedContents(stackedItemContents);
        }
    }

//	public void clearCraftingContent() {
//		this.getSlot(INGREDIENT_SLOT).set(ItemStack.EMPTY);
//		this.getSlot(RESULT_SLOT).set(ItemStack.EMPTY);
//	}
//
//	public boolean recipeMatches(Recipe<? super Container> recipe) {
//		return recipe.matches(this.container, this.level);
//	}
//
//	public int getResultSlotIndex() {
//		return RESULT_SLOT;
//	}
//
//	public int getGridWidth() {
//		return 1;
//	}
//
//	public int getGridHeight() {
//		return 1;
//	}
//
//	public int getSize() {
//		return SLOT_COUNT;
//	}
//
//	public boolean stillValid(Player player) {
//		return this.container.stillValid(player);
//	}
    
    public Slot getResultSlot() {
        return this.slots.get(RESULT_SLOT);
    }

    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player inventory and the other inventory(s).
     */
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index == RESULT_SLOT) {
				if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (index != INGREDIENT_SLOT) { // slotIndex != FUEL_SLOT &&
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
				else if (index >= INV_SLOT_START && index < INV_SLOT_END) {
					if (!this.moveItemStackTo(itemstack1, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= USE_ROW_SLOT_START && index < USE_ROW_SLOT_END && !this.moveItemStackTo(itemstack1, INV_SLOT_START, INV_SLOT_END, false)) {
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

    protected boolean canSmelt(ItemStack stack) {
        return this.acceptedInputs.test(stack);
    }

    protected boolean isFuel(ItemStack stack) {
        return stack.getBurnTime(this.recipeType, this.level.fuelValues()) > 0;
    }

    public float getBurnProgress() {
        int i = this.data.get(2);
        int j = this.data.get(3);
        return j != 0 && i != 0 ? Mth.clamp((float)i / j, 0.0F, 1.0F) : 0.0F;
    }

    public float getLitProgress() {
        int i = this.data.get(1);
        if (i == 0) {
            i = 200;
        }

        return Mth.clamp((float)this.data.get(0) / i, 0.0F, 1.0F);
    }

    public boolean isLit() {
        return this.data.get(0) > 0;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return this.recipeBookType;
    }

    @Override
    public RecipeBookMenu.PostPlaceAction handlePlacement(
        boolean useMaxItems, boolean isCreative, RecipeHolder<?> recipe, final ServerLevel level, Inventory playerInventory
    ) {
        final List<Slot> list = List.of(this.getSlot(0), this.getSlot(2));
        return ServerPlaceRecipe.placeRecipe(new ServerPlaceRecipe.CraftingMenuAccess<AbstractCookingRecipe>() {
            @Override
            public void fillCraftSlotsStackedContents(StackedItemContents stackedItemContents) {
                AbstractNoFuelFurnaceMenu.this.fillCraftSlotsStackedContents(stackedItemContents);
            }

            @Override
            public void clearCraftingContent() {
                list.forEach(p_362814_ -> p_362814_.set(ItemStack.EMPTY));
            }

            @Override
            public boolean recipeMatches(RecipeHolder<AbstractCookingRecipe> p_recipe) {
                return p_recipe.value().matches(new SingleRecipeInput(AbstractNoFuelFurnaceMenu.this.container.getItem(0)), level);
            }
        }, 1, 1, List.of(this.getSlot(0)), list, playerInventory, (RecipeHolder<AbstractCookingRecipe>)recipe, useMaxItems, isCreative);
    }
}