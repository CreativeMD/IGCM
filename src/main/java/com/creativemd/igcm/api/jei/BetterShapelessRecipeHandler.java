package com.creativemd.igcm.api.jei;

import javax.annotation.Nonnull;

import com.creativemd.creativecore.common.recipe.BetterShapelessRecipe;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.util.ErrorUtil;
import mezz.jei.util.Log;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;

public class BetterShapelessRecipeHandler implements IRecipeHandler<BetterShapelessRecipe> {
	@Nonnull
	private final IGuiHelper guiHelper;

	public BetterShapelessRecipeHandler(@Nonnull IGuiHelper guiHelper) {
		this.guiHelper = guiHelper;
	}

	@Override
	@Nonnull
	public Class<BetterShapelessRecipe> getRecipeClass() {
		return BetterShapelessRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return VanillaRecipeCategoryUid.CRAFTING;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull BetterShapelessRecipe recipe) {
		return VanillaRecipeCategoryUid.CRAFTING;
	}

	@Override
	@Nonnull
	public IRecipeWrapper getRecipeWrapper(@Nonnull BetterShapelessRecipe recipe) {
		return new BetterShapelessRecipeWrapper(guiHelper, recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull BetterShapelessRecipe recipe) {
		return true;
	}
}