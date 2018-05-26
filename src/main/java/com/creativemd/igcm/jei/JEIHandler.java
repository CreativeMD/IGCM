package com.creativemd.igcm.jei;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import mezz.jei.JustEnoughItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.recipe.IRecipeWrapper;
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
	
}
