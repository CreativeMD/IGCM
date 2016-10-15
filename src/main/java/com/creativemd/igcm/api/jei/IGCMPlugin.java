package com.creativemd.igcm.api.jei;

import javax.annotation.Nonnull;

import com.creativemd.igcm.api.core.IGCM;
import com.creativemd.igcm.mod.ConfigManagerModLoader;
import com.creativemd.igcm.mod.block.AdvancedWorkbench;

import mezz.jei.JeiRuntime;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class IGCMPlugin extends BlankModPlugin {
	
	
	
	@Override
	public void register(@Nonnull IModRegistry registry) {
		JEIHandler.isActive = true;
		JEIHandler.registry = registry;
		
		
		IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
		
		registry.addRecipeCategories(new AdvCraftingRecipeCategory(guiHelper));
		
		registry.addRecipeHandlers(new BetterShapedRecipeHandler(), new BetterShapelessRecipeHandler(guiHelper), new AdvRecipeHandler());
		
		registry.addRecipeCategoryCraftingItem(new ItemStack(IGCM.advancedWorkbench), AdvCraftingRecipeCategory.CategoryUiD);
		
		registry.addRecipes(ConfigManagerModLoader.advancedWorkbench.getAllExitingRecipes());
	}
	
}
