package com.creativemd.igcm.jei;

import javax.annotation.Nonnull;

import com.creativemd.creativecore.common.recipe.BetterShapedRecipe;

import java.util.List;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.util.ErrorUtil;
import mezz.jei.util.Log;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BetterShapedRecipeHandler implements IRecipeHandler<BetterShapedRecipe> {

	@Override
	@Nonnull
	public Class<BetterShapedRecipe> getRecipeClass() {
		return BetterShapedRecipe.class;
	}
	
	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull BetterShapedRecipe recipe) {
		return VanillaRecipeCategoryUid.CRAFTING;
	}

	@Override
	@Nonnull
	public IRecipeWrapper getRecipeWrapper(@Nonnull BetterShapedRecipe recipe) {
		return new BetterShapedRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull BetterShapedRecipe recipe) {
		return true;
	}
}