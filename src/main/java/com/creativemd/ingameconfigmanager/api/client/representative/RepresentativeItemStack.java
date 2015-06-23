package com.creativemd.ingameconfigmanager.api.client.representative;

import com.creativemd.creativecore.client.rendering.RenderHelper2D;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

public class RepresentativeItemStack extends RepresentativeObject{
	
	public ItemStack stack;
	
	public RepresentativeItemStack(ItemStack stack)
	{
		this.stack = stack;
	}
	
	@Override
	public void handleRendering(Minecraft mc, FontRenderer fontRenderer, int width, int height) {
		RenderHelper2D.renderItem(stack, width/2-16/2, height/2-16/2);
	}
}
