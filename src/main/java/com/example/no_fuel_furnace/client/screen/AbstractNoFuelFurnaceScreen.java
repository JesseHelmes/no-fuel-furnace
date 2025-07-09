package com.example.no_fuel_furnace.client.screen;

import java.util.List;

import com.example.no_fuel_furnace.menu.AbstractNoFuelFurnaceMenu;
import  com.example.no_fuel_furnace.client.screen.recipebook.NoFuelFurnaceRecipeBookComponent;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

// copy of AbstractFurnaceScreen
public abstract class AbstractNoFuelFurnaceScreen <T extends AbstractNoFuelFurnaceMenu> extends AbstractRecipeBookScreen<T> {
    private final ResourceLocation texture;
    private final ResourceLocation litProgressSprite;
    private final ResourceLocation burnProgressSprite;

	public AbstractNoFuelFurnaceScreen(
        T menu,
        Inventory playerInventory,
        Component title,
        Component recipeFilterName,
        ResourceLocation texture,
        ResourceLocation litProgressSprite,
        ResourceLocation burnProgressSprite,
        List<RecipeBookComponent.TabInfo> tabInfos
	) {
        super(menu, new NoFuelFurnaceRecipeBookComponent(menu, recipeFilterName, tabInfos), playerInventory, title);
        this.texture = texture;
        this.litProgressSprite = litProgressSprite;
        this.burnProgressSprite = burnProgressSprite;
	}

    @Override
    public void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected ScreenPosition getRecipeBookButtonPosition() {
        return new ScreenPosition(this.leftPos + 20, this.height / 2 - 49);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		int i = this.leftPos;
		int j = this.topPos;
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, this.texture, i, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
		if (this.menu.isLit()) {
            int k = 14;
            int l = Mth.ceil(this.menu.getLitProgress() * 13.0F) + 1;
         // Offset to move the input from minecraft orignal place
            int ingredientOffsetFromOrignal = 17;
			guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.litProgressSprite, 14, 14, 0, 14 - l, i + 56, j + 36 + 14 - l + ingredientOffsetFromOrignal, 14, l);
		}

		int i1 = 24;
        int j1 = Mth.ceil(this.menu.getBurnProgress() * 24.0F);
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.burnProgressSprite, 24, 16, 0, 0, i + 79, j + 34, j1, 16);
	}

}