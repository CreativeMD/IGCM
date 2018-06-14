package com.creativemd.igcm.jei;

import javax.annotation.Nonnull;

import com.creativemd.creativecore.common.recipe.BetterShapedRecipe;

import java.util.List;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.util.ErrorUtil;
import mezz.jei.util.Log;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BetterShapedRecipeFactory implements IRecipeWrapperFactory<BetterShapedRecipe> {

	@Override
	@Nonnull
	public IRecipeWrapper getRecipeWrapper(@Nonnull BetterShapedRecipe recipe) {
		return new BetterShapedRecipeWrapper(recipe);
	}
	
}