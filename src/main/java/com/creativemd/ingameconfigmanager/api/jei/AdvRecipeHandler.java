package com.creativemd.ingameconfigmanager.api.jei;

import javax.annotation.Nonnull;

import com.creativemd.creativecore.common.recipe.BetterShapedRecipe;
import com.creativemd.ingameconfigmanager.mod.block.AdvancedGridRecipe;

import java.util.List;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.util.ErrorUtil;
import mezz.jei.util.Log;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class AdvRecipeHandler implements IRecipeHandler<AdvancedGridRecipe> {

	@Override
	@Nonnull
	public Class<AdvancedGridRecipe> getRecipeClass() {
		return AdvancedGridRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return AdvCraftingRecipeCategory.CategoryUiD;
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