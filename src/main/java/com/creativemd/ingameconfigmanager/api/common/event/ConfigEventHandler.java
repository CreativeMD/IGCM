package com.creativemd.ingameconfigmanager.api.common.event;

import java.util.ArrayList;

import com.creativemd.ingameconfigmanager.api.client.representative.RepresentativeIcon;

import net.minecraftforge.client.event.TextureStitchEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ConfigEventHandler {
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onStitch(TextureStitchEvent.Pre event)
	{
		ArrayList<RepresentativeIcon> iconList = RepresentativeIcon.getIconList();
		
		for(int i = 0; i < iconList.size(); i++)
		{
			String iconPath = iconList.get(i).iconPath;
			if((iconList.get(i).isItem && event.map.getTextureType() == 1) || (!iconList.get(i).isItem && event.map.getTextureType() == 0))
			{
				event.map.registerIcon(iconPath);
			}
		}
	}
	
}
