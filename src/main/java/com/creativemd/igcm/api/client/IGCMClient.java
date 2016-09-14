package com.creativemd.igcm.api.client;

import org.lwjgl.input.Keyboard;

import com.creativemd.creativecore.core.CreativeCoreClient;
import com.creativemd.igcm.api.core.IGCM;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class IGCMClient {
	
	public static KeyBinding openConfig;

	
	public static void initClient()
	{
		CreativeCoreClient.registerBlockItem(IGCM.advancedWorkbench);
		
		openConfig = new KeyBinding("key.openConfig", Keyboard.KEY_V, "key.categories.IGCM");
		ClientRegistry.registerKeyBinding(openConfig);
		
		MinecraftForge.EVENT_BUS.register(new IGCMClientTick());
	}
	
}
