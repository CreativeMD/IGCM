package com.creativemd.igcm.jei;

import java.util.List;

import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import net.minecraftforge.client.model.ItemLayerModel.Loader;
import net.minecraftforge.fml.common.Optional.Method;

public class JEIHandler {
	
	public static boolean isActive = false;
	
	public static Object recipeRegistry;
	
	public static Object modRegistry;
	
	public static void removeRecipes(List recipes)
	{
		if(isActive && recipeRegistry != null)
			removeRecipesInternal(recipes);
	}
	
	@Method(modid = "jei")
	private static void removeRecipesInternal(List recipes)
	{
		for (int i = 0; i < recipes.size(); i++) {
			((IRecipeRegistry) recipeRegistry).removeRecipe(recipes.get(i));
		}
	}
	
	public static void addRecipes(List recipes)
	{
		if(isActive && recipeRegistry != null)
			addRecipesInternal(recipes);
	}
	
	@Method(modid = "jei")
	private static void addRecipesInternal(List recipes)
	{
		for (int i = 0; i < recipes.size(); i++) {
			((IRecipeRegistry) recipeRegistry).addRecipe(recipes.get(i));
		}
	}
	
}
