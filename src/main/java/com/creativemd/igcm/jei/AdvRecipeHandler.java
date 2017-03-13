package com.creativemd.igcm.jei;

import javax.annotation.Nonnull;

import com.creativemd.igcm.block.AdvancedGridRecipe;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class AdvRecipeHandler implements IRecipeHandler<AdvancedGridRecipe> {

	@Override
	@Nonnull
	public Class<AdvancedGridRecipe> getRecipeClass() {
		return AdvancedGridRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull AdvancedGridRecipe recipe) {
		return AdvCraftingRecipeCategory.CategoryUiD;
	}

	@Override
	@Nonnull
	public IRecipeWrapper getRecipeWrapper(@Nonnull AdvancedGridRecipe recipe) {
		return new AdvRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull AdvancedGridRecipe recipe) {
		for (int i = 0; i < recipe.output.length; i++) {
			if(recipe.output[i] != null && recipe.output[i].getItem() == null)
				return false;
		}
		return true;
	}
}