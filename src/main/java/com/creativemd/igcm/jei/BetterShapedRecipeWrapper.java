package com.creativemd.igcm.jei;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import com.creativemd.creativecore.common.recipe.BetterShapedRecipe;

import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.item.ItemStack;

public class BetterShapedRecipeWrapper implements IShapedCraftingRecipeWrapper {
	
	@Nonnull
	private final BetterShapedRecipe recipe;
	
	public BetterShapedRecipeWrapper(@Nonnull BetterShapedRecipe recipe) {
		this.recipe = recipe;
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
		IStackHelper stackHelper = ((IModRegistry) JEIHandler.modRegistry).getJeiHelpers().getStackHelper();
		
		List<List<ItemStack>> inputs = stackHelper.expandRecipeItemStackInputs(Arrays.asList(recipe.getInput()));
		ingredients.setInputLists(ItemStack.class, inputs);
		
		ItemStack recipeOutput = recipe.getRecipeOutput();
		if (recipeOutput != null) {
			ingredients.setOutput(ItemStack.class, recipeOutput);
		}
	}
	
}
