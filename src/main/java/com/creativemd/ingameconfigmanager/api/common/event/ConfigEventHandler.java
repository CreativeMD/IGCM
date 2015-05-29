package com.creativemd.ingameconfigmanager.api.common.event;

import java.util.ArrayList;

import com.creativemd.ingameconfigmanager.api.client.representative.RepresentativeIcon;

import net.minecraftforge.client.event.TextureStitchEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
