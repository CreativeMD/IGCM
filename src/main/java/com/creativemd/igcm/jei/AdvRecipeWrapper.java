package com.creativemd.igcm.jei;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import com.creativemd.igcm.block.AdvancedGridRecipe;

import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.item.ItemStack;

public class AdvRecipeWrapper  implements IRecipeWrapper, IShapedCraftingRecipeWrapper {
	
	@Nonnull
	private final AdvancedGridRecipe recipe;

	public AdvRecipeWrapper(@Nonnull AdvancedGridRecipe recipe) {
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
		IStackHelper stackHelper = ((IModRegistry)JEIHandler.modRegistry).getJeiHelpers().getStackHelper();

		List<List<ItemStack>> inputs = stackHelper.expandRecipeItemStackInputs(Arrays.asList(recipe.getInputStacks()));
		ingredients.setInputLists(ItemStack.class, inputs);

		ItemStack[] recipeOutput = recipe.output;
		if (recipeOutput != null) {
			ingredients.setOutputs(ItemStack.class, Arrays.asList(recipeOutput));
		}
	}

	
}
