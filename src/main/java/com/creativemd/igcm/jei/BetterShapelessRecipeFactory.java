package com.creativemd.igcm.jei;

import javax.annotation.Nonnull;

import com.creativemd.creativecore.common.recipe.BetterShapelessRecipe;

import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

public class BetterShapelessRecipeFactory implements IRecipeWrapperFactory<BetterShapelessRecipe> {
	
	@Override
	@Nonnull
	public IRecipeWrapper getRecipeWrapper(@Nonnull BetterShapelessRecipe recipe) {
		return new BetterShapelessRecipeWrapper(recipe);
	}
	
}