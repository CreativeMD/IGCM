package com.creativemd.igcm.jei;

import javax.annotation.Nonnull;

import com.creativemd.igcm.block.AdvancedGridRecipe;

import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

public class AdvRecipeFactory implements IRecipeWrapperFactory<AdvancedGridRecipe> {
	
	@Override
	@Nonnull
	public IRecipeWrapper getRecipeWrapper(@Nonnull AdvancedGridRecipe recipe) {
		return new AdvRecipeWrapper(recipe);
	}
}