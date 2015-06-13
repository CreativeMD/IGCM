package com.creativemd.ingameconfigmanager.api.tab.core;

import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.creativemd.ingameconfigmanager.api.common.event.ConfigEventHandler;
import com.creativemd.ingameconfigmanager.api.tab.HeadTab;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * Copyright 2015 CreativeMD & N247S
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

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

