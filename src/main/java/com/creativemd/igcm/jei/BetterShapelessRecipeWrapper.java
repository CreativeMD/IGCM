package com.creativemd.igcm.jei;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import com.creativemd.creativecore.common.recipe.BetterShapelessRecipe;

import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.item.ItemStack;

public class BetterShapelessRecipeWrapper implements IShapedCraftingRecipeWrapper {
	
	@Nonnull
	private final BetterShapelessRecipe recipe;
	
	public BetterShapelessRecipeWrapper(@Nonnull BetterShapelessRecipe recipe) {
		super();
		this.recipe = recipe;
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
	
	@Override
	public int getWidth() {
		return recipe.getWidth();
	}
	
	@Override
	public int getHeight() {
		return recipe.getInput().length / recipe.getWidth();
	}
	
}