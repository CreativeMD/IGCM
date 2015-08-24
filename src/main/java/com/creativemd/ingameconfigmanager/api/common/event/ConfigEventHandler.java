package com.creativemd.ingameconfigmanager.api.common.event;

/*
 * Content
 * - IconRegistryEvents
 * - MouseEvents
 * - KeyboardEvents
 */

import java.awt.Toolkit;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.TextureStitchEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.creativemd.creativecore.client.avatar.AvatarIcon;
import com.creativemd.ingameconfigmanager.api.core.InGameConfigManager;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ConfigEventHandler
{
	//////////////////////////////
	/*
	 * IconRegistryEvents
	 */
	//////////////////////////////
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onStitch(TextureStitchEvent.Pre event)
	{
		ArrayList<AvatarIcon> iconList = AvatarIcon.getIconList();
		
		for(int i = 0; i < iconList.size(); i++)
		{
			String iconPath = iconList.get(i).iconPath;
			if((iconList.get(i).isItem && event.map.getTextureType() == 1) || (!iconList.get(i).isItem && event.map.getTextureType() == 0))
			{
				event.map.registerIcon(iconPath);
			}
		}
	}
	
	//////////////////////////////
	/*
	* OnPlayerLogin
	*/
	//////////////////////////////
	@SubscribeEvent
	public void onPlayerJoin(PlayerLoggedInEvent event)
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isServer())
			InGameConfigManager.sendAllUpdatePackets(event.player);
	}
}
