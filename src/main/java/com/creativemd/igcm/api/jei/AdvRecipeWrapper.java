package com.creativemd.igcm.api.jei;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import com.creativemd.igcm.mod.block.AdvancedGridRecipe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.item.ItemStack;

public class AdvRecipeWrapper extends BlankRecipeWrapper implements IShapedCraftingRecipeWrapper {
	
	@Nonnull
	private final AdvancedGridRecipe recipe;

	public AdvRecipeWrapper(@Nonnull AdvancedGridRecipe recipe) {
		this.recipe = recipe;
	}

	@Nonnull
	@Override
	public List getInputs() {
		return Arrays.asList(recipe.getInputStacks());
	}

	@Nonnull
	@Override
	public List<ItemStack> getOutputs() {		
		return Arrays.asList(recipe.output);
	}

	@Override
	public int getWidth() {
		return recipe.width;
	}

	@Override
	public int getHeight() {
		return recipe.height;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		
	}

	
}
