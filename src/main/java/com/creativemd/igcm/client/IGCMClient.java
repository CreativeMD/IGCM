package com.creativemd.igcm.client;

import org.lwjgl.input.Keyboard;

import com.creativemd.creativecore.core.CreativeCoreClient;
import com.creativemd.igcm.IGCM;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class IGCMClient {
	
	public static KeyBinding openConfig;
	
	public static void initClientPre()
	{
		
	}
	
	public static void initClient()
	{
		CreativeCoreClient.registerBlockItem(IGCM.advancedWorkbenchBlock);
		
		openConfig = new KeyBinding("key.openConfig", Keyboard.KEY_V, "key.categories.IGCM");
		ClientRegistry.registerKeyBinding(openConfig);
		
		MinecraftForge.EVENT_BUS.register(new IGCMClientTick());
	}
	
}
