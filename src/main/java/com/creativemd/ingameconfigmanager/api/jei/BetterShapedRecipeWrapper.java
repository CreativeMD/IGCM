package com.creativemd.ingameconfigmanager.api.jei;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.creativemd.creativecore.common.recipe.BetterShapedRecipe;

import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import mezz.jei.plugins.vanilla.VanillaRecipeWrapper;
import net.minecraft.item.ItemStack;

public class BetterShapedRecipeWrapper extends VanillaRecipeWrapper implements IShapedCraftingRecipeWrapper {
	
	@Nonnull
	private final BetterShapedRecipe recipe;

	public BetterShapedRecipeWrapper(@Nonnull BetterShapedRecipe recipe) {
		this.recipe = recipe;
	}

	@Nonnull
	@Override
	public List getInputs() {
		return Arrays.asList(recipe.getInput());
	}

	@Nonnull
	@Override
	public List<ItemStack> getOutputs() {
		return Collections.singletonList(recipe.getRecipeOutput());
	}

	@Override
	public int getWidth() {
		return recipe.width;
	}

	@Override
	public int getHeight() {
		return recipe.height;
	}

	
}
