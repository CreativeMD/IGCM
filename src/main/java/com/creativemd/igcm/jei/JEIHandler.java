package com.creativemd.igcm.jei;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mezz.jei.JustEnoughItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.collect.ListMultiMap;
import mezz.jei.collect.MultiMap;
import mezz.jei.collect.SetMultiMap;
import mezz.jei.collect.Table;
import mezz.jei.ingredients.Ingredients;
import mezz.jei.recipes.RecipeMap;
import mezz.jei.recipes.RecipeRegistry;
import mezz.jei.startup.JeiStarter;
import mezz.jei.startup.ProxyCommonClient;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.model.ItemLayerModel.Loader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.server.FMLServerHandler;

public class JEIHandler {
	
	public static boolean isActive = false;
	
	public static Field plugins;
	public static Field starter;
	
	public static Object modRegistry;
	
	public static Object recipeRegistry;
	
	private static Field hiddenRecipesField;
	private static Field wrapperMapsField;
	private static Field recipeInputMapField;
	private static Field recipeOutputMapField;
	
	private static Field recipeWrapperTableField;
	private static Field categoryUidMapField;
	
	private static Field recipeWrappersForCategoriesField;
	private static Field recipeCategoriesVisibleCacheField;
	
	private static Field mapMultiMapField;
	
	private static Field tableField;
	
	private static void loadReflections()
	{
		if(hiddenRecipesField == null)
		{
			hiddenRecipesField = ReflectionHelper.findField(RecipeRegistry.class, "hiddenRecipes");
			wrapperMapsField = ReflectionHelper.findField(RecipeRegistry.class, "wrapperMaps");
			recipeInputMapField = ReflectionHelper.findField(RecipeRegistry.class, "recipeInputMap");
			recipeOutputMapField = ReflectionHelper.findField(RecipeRegistry.class, "recipeOutputMap");
			
			recipeWrapperTableField = ReflectionHelper.findField(RecipeMap.class, "recipeWrapperTable");
			categoryUidMapField = ReflectionHelper.findField(RecipeMap.class, "categoryUidMap");
			
			recipeWrappersForCategoriesField = ReflectionHelper.findField(RecipeRegistry.class, "recipeWrappersForCategories");
			recipeCategoriesVisibleCacheField = ReflectionHelper.findField(RecipeRegistry.class, "recipeCategoriesVisibleCache");
			
			mapMultiMapField = ReflectionHelper.findField(MultiMap.class, "map");
			tableField = ReflectionHelper.findField(Table.class, "table");
		}
	}
	
	private static void clearMultiMap(Object multimap, Object key)
	{
		try {
			((Map) mapMultiMapField.get(multimap)).remove(key);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private static void clearTable(Object table, Object key)
	{
		try {
			((Map) tableField.get(table)).remove(key);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public static void clearCategory(String categoryId)
	{
		loadReflections();
		
		try {
			IRecipeCategory category = ((IRecipeRegistry) JEIHandler.recipeRegistry).getRecipeCategory(categoryId);
			
			if(category == null)
				return ;
			
			clearMultiMap(hiddenRecipesField.get(recipeRegistry), categoryId);
			
			clearTable(wrapperMapsField.get(recipeRegistry), categoryId);
			
			RecipeMap recipeInputMap = (RecipeMap) recipeInputMapField.get(recipeRegistry);
			clearTable(recipeWrapperTableField.get(recipeInputMap), category);
			for (Iterator<Entry<String, List<String>>> iterator = ((ListMultiMap<String, String>) categoryUidMapField.get(recipeInputMap)).entrySet().iterator(); iterator.hasNext();) {
				Entry<String, List<String>> entry = iterator.next();
				entry.getValue().remove(categoryId);
				if(entry.getValue().isEmpty())
					iterator.remove();
			}
			
			RecipeMap recipeOutputMap = (RecipeMap) recipeOutputMapField.get(recipeRegistry);
			clearTable(recipeWrapperTableField.get(recipeOutputMap), category);
			for (Iterator<Entry<String, List<String>>> iterator = ((ListMultiMap<String, String>) categoryUidMapField.get(recipeOutputMap)).entrySet().iterator(); iterator.hasNext();) {
				Entry<String, List<String>> entry = iterator.next();
				entry.getValue().remove(categoryId);
				if(entry.getValue().isEmpty())
					iterator.remove();
			}
			
			clearMultiMap(recipeWrappersForCategoriesField.get(recipeRegistry), category);
		
			((List) recipeCategoriesVisibleCacheField.get(recipeRegistry)).clear();
			
			
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private static boolean silentGemRecipes = false;
	private static Field exampleRecipes = null;
	
	public static boolean isSilentGemInstalled()
	{
		if(!silentGemRecipes)
		{
			try {
				Class toolHelperClass = Class.forName("net.silentchaos512.gems.util.ToolHelper");
				if(toolHelperClass != null)
					exampleRecipes = ReflectionHelper.findField(toolHelperClass, "EXAMPLE_RECIPES");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			silentGemRecipes = true;
		}
		return exampleRecipes != null;
	}
	
	public static void addSilentGemRecipes()
	{
		try {
			((IModRegistry) modRegistry).addRecipes((Collection<?>) exampleRecipes.get(null), VanillaRecipeCategoryUid.CRAFTING);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
}
