package com.creativemd.igcm.jei;

import javax.annotation.Nonnull;

import com.creativemd.creativecore.common.recipe.BetterShapedRecipe;
import com.creativemd.creativecore.common.recipe.BetterShapelessRecipe;
import com.creativemd.igcm.IGCM;
import com.creativemd.igcm.block.AdvancedGridRecipe;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class IGCMPlugin implements IModPlugin {
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new AdvCraftingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
	}
	
	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		JEIHandler.recipeRegistry = jeiRuntime.getRecipeRegistry();
	}
	
	@Override
	public void register(@Nonnull IModRegistry registry) {
		JEIHandler.isActive = true;
		
		JEIHandler.modRegistry = registry;
		
		registry.handleRecipes(BetterShapedRecipe.class, new BetterShapedRecipeFactory(), VanillaRecipeCategoryUid.CRAFTING);
		registry.handleRecipes(BetterShapelessRecipe.class, new BetterShapelessRecipeFactory(), VanillaRecipeCategoryUid.CRAFTING);
		
		registry.handleRecipes(AdvancedGridRecipe.class, new AdvRecipeFactory(), AdvCraftingRecipeCategory.CategoryUiD);
		
		registry.addRecipeCatalyst(new ItemStack(IGCM.advancedWorkbenchBlock), AdvCraftingRecipeCategory.CategoryUiD);
	}
	
}
