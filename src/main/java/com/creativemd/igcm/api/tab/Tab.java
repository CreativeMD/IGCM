package com.creativemd.igcm.api.tab;

import com.creativemd.creativecore.client.avatar.Avatar;
import com.creativemd.creativecore.client.avatar.AvatarItemStack;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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