package com.creativemd.ingameconfigmanager.api.tab;

import com.creativemd.creativecore.client.avatar.Avatar;
import com.creativemd.creativecore.client.avatar.AvatarItemStack;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

public abstract class Tab {
	
	public String title;
	
	@SideOnly(Side.CLIENT)
	public Avatar avatar;
	
	public Tab(String title, ItemStack stack)
	{
		this.title = title;
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
			initClient(stack);
	}
	
	@SideOnly(Side.CLIENT)
	public void initClient(ItemStack stack)
	{
		this.avatar = new AvatarItemStack(stack);
	}
	
}