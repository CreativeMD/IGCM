package com.creativemd.ingameconfigmanager.api.jei;

import javax.annotation.Nonnull;

import com.creativemd.ingameconfigmanager.api.core.InGameConfigManager;
import com.creativemd.ingameconfigmanager.mod.ConfigManagerModLoader;
import com.creativemd.ingameconfigmanager.mod.block.AdvancedWorkbench;

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
		
		registry.addRecipeCategoryCraftingItem(new ItemStack(InGameConfigManager.advancedWorkbench), AdvCraftingRecipeCategory.CategoryUiD);
		
		registry.addRecipes(ConfigManagerModLoader.advancedWorkbench.getAllExitingRecipes());
	}
	
}
