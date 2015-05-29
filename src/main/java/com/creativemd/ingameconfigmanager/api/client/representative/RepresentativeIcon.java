package com.creativemd.ingameconfigmanager.api.client.representative;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.client.rendering.RenderHelper2D;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.IIcon;

/**
 * How to register a custom icon:
 * @SideOnly(Side.CLIENT)
 * @SubscribeEvent
 * public void onStitch(TextureStitchEvent.Pre event)
 * {
 * 	  	if(event.map.getTextureType() == 0)
 * 		{
 * 			yourIcon = event.map.registerIcon(YourMod.MODID + ":yourIconName");
 * 		}
 * }**/

public class RepresentativeIcon extends RepresentativeObject
{
	private static ArrayList<RepresentativeIcon> loadingList = new ArrayList<RepresentativeIcon>();
	
	public IIcon icon;
	public String iconPath;
	public boolean isItem;
	
	public RepresentativeIcon(IIcon icon)
	{
		this.icon = icon;
	}
	
	public RepresentativeIcon(String iconPath)
	{
		this(iconPath, true);
	}
	
	public RepresentativeIcon(String iconPath, boolean isItem)
	{
		this.iconPath = iconPath;
		loadingList.add(this);
		this.isItem = isItem;
	}

	@Override
	public void handleRendering(Minecraft mc, FontRenderer fontRenderer, int width, int height) {
		if(icon != null)
		{
			RenderHelper2D.renderIcon(icon, width/2-16/2, height/2-16/2, 1, true, 0, 16, 16);
		}
	}
	
	public static ArrayList getIconList()
	{
		return loadingList;
	}
	
}
