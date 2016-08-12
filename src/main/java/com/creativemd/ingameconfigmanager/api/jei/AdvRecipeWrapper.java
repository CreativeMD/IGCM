package com.creativemd.ingameconfigmanager.api.jei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.creativemd.creativecore.common.recipe.BetterShapedRecipe;
import com.creativemd.ingameconfigmanager.mod.block.AdvancedGridRecipe;

import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import mezz.jei.plugins.vanilla.VanillaRecipeWrapper;
import net.minecraft.item.ItemStack;

public class AdvRecipeWrapper extends VanillaRecipeWrapper implements IShapedCraftingRecipeWrapper {
	
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

	
}
