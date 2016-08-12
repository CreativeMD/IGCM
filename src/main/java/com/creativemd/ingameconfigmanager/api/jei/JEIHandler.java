package com.creativemd.ingameconfigmanager.api.jei;

import mezz.jei.api.IModRegistry;
import net.minecraftforge.client.model.ItemLayerModel.Loader;
import net.minecraftforge.fml.common.Optional.Method;

public class JEIHandler {
	
	public static boolean isActive = false;
	
	public static void forceReload()
	{
		if(isActive && registry != null)
			forceReloadPrivate();
	}
	
	public static Object registry;
	
	@Method(modid = "JEI")
	private static void forceReloadPrivate()
	{
		((IModRegistry)registry).getJeiHelpers().reload();
	}
	
}
