package com.creativemd.ingameconfigmanager.api.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import mezz.jei.util.Log;
import mezz.jei.util.Translator;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

import com.creativemd.ingameconfigmanager.api.core.IGCM;
import com.creativemd.ingameconfigmanager.mod.block.BlockAdvancedWorkbench;

public class AdvCraftingRecipeCategory extends BlankRecipeCategory<ICraftingRecipeWrapper> {
	
	public static final String CategoryUiD = "igcm.advcrafting";

	private static final int craftOutputSlot = 0;
	private static final int craftInputSlot1 = BlockAdvancedWorkbench.outputs;

	public static final int width = 116;
	public static final int height = 54;

	@Nonnull
	private final IDrawable background;
	@Nonnull
	private final String localizedName;
	@Nonnull
	private final ICraftingGridHelper craftingGridHelper;

	public AdvCraftingRecipeCategory(IGuiHelper guiHelper) {
		ResourceLocation location = new ResourceLocation("minecraft", "textures/gui/container/crafting_table.png");
		background = guiHelper.createBlankDrawable(200, 200);//.createDrawable(location, 29, 16, width, height);
		localizedName = Translator.translateToLocal("Advanced Crafting Table");
		craftingGridHelper = guiHelper.createCraftingGridHelper(craftInputSlot1, craftOutputSlot);
	}

	@Override
	@Nonnull
	public String getUid() {
		return CategoryUiD;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return localizedName;
	}

	@Override
	@Nonnull
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull ICraftingRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		
		for (int i = 0; i < BlockAdvancedWorkbench.outputs; i++) {
			guiItemStacks.init(craftOutputSlot+i, false, 94, 18+i*18);
		}
		

		for (int y = 0; y < BlockAdvancedWorkbench.gridSize; ++y) {
			for (int x = 0; x < BlockAdvancedWorkbench.gridSize; ++x) {
				int index = craftInputSlot1 + x + (y * BlockAdvancedWorkbench.gridSize);
				guiItemStacks.init(index, true, x * 18, y * 18);
			}
		}
		
		if(recipeWrapper instanceof AdvRecipeWrapper)
		{
			AdvRecipeWrapper wrapper = (AdvRecipeWrapper) recipeWrapper;
			craftingGridHelper.setInput(guiItemStacks, wrapper.getInputs(), wrapper.getWidth(), wrapper.getHeight());
			craftingGridHelper.setOutput(guiItemStacks, wrapper.getOutputs());
		}

		/*if (recipeWrapper instanceof IShapedCraftingRecipeWrapper) {
			IShapedCraftingRecipeWrapper wrapper = (IShapedCraftingRecipeWrapper) recipeWrapper;
			craftingGridHelper.setInput(guiItemStacks, wrapper.getInputs(), wrapper.getWidth(), wrapper.getHeight());
			craftingGridHelper.setOutput(guiItemStacks, wrapper.getOutputs());
		} else {
			craftingGridHelper.setInput(guiItemStacks, recipeWrapper.getInputs());
			craftingGridHelper.setOutput(guiItemStacks, recipeWrapper.getOutputs());
		}*/
	}

}