package com.creativemd.ingameconfigmanager.core;

import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.creativemd.ingameconfigmanager.api.common.event.ConfigEventHandler;
import com.creativemd.ingameconfigmanager.api.tab.HeadTab;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = InGameConfigManager.modid, version = InGameConfigManager.version, name = "InGameConfigManager")
public class InGameConfigManager { 
	
	public static Logger logger = LogManager.getLogger(InGameConfigManager.modid);
	
	public static final String modid = "ingameconfigmanager";
	
	public static final String version = "1.0.0";
	
	public static ConfigEventHandler eventHandler = new ConfigEventHandler();
	
	//public static HeadTab tabs = new HeadTab("CraftingManager", new ReIcon("sdkfdksjfk")));
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(eventHandler);
	} 

}

