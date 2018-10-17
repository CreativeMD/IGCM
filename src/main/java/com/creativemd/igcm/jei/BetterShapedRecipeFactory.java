package com.creativemd.igcm.jei;

import javax.annotation.Nonnull;

import com.creativemd.creativecore.common.recipe.BetterShapedRecipe;

import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

public class BetterShapedRecipeFactory implements IRecipeWrapperFactory<BetterShapedRecipe> {
	
	@Override
	@Nonnull
	public IRecipeWrapper getRecipeWrapper(@Nonnull BetterShapedRecipe recipe) {
		return new BetterShapedRecipeWrapper(recipe);
	}
	
}