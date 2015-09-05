package com.creativemd.ingameconfigmanager.api.client.gui;

import javax.vecmath.Vector4d;

import com.creativemd.creativecore.client.rendering.RenderHelper2D;
import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.common.gui.controls.GuiButton;
import com.creativemd.creativecore.common.gui.controls.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.GuiTextfield;
import com.creativemd.creativecore.common.gui.event.ControlClickEvent;
import com.creativemd.creativecore.common.utils.stack.StackInfoItemStack;
import com.creativemd.ingameconfigmanager.api.common.container.controls.GuiInvSelector;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SubGuiItemDialog extends SubGui{
	
	public ItemStack stack;
	
	public SubGuiItemDialog()
	{
		super(150, 80);
	}
	
	@Override
	public void createControls() {
		GuiInvSelector selector = new GuiInvSelector("inv", 5, 5, 140, container.player, false);
		controls.add(selector);
		if(stack != null)
			selector.addAndSelectStack(stack);
		
		controls.add(new GuiLabel("StackSize:", 5, 30));		
		GuiTextfield field = new GuiTextfield("stacksize", "1", 110, 30, 30, 20).setNumbersOnly();
		if(stack != null)
			field.text = "" + stack.stackSize;
		controls.add(field);
		
		controls.add(new GuiButton("Cancel", 5, 55, 60, 20));
		controls.add(new GuiButton("Save", 85, 55, 60, 20));
	}

	@Override
	public void drawOverlay(FontRenderer fontRenderer) {
		
	}
	
	@CustomEventSubscribe
	public void onClicked(ControlClickEvent event)
	{
		if(event.source.is("Save"))
		{
			stack = ((GuiInvSelector)getControl("inv")).getStack();
			int stacksize = 1;
			try{
				stacksize = Integer.parseInt(((GuiTextfield)getControl("stacksize")).text);
			}catch (Exception e){
				stacksize = 1;
			}
			if(stack != null)
			{
				stack = stack.copy();
				stack.stackSize = stacksize;
			}
			
			closeLayer(new NBTTagCompound());
		}
		if(event.source.is("Cancel"))
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("canceled", true);
			closeLayer(nbt);
		}
	}
	
	@Override
	public void drawBackground()
	{
		int k = (this.width - this.width) / 2;
		int l = (this.height - this.height) / 2;
		
		Vector4d color = new Vector4d(0, 0, 0, 255);
		RenderHelper2D.drawGradientRect(k, l, k+this.width, l+this.height, color, color);
		color = new Vector4d(120, 120, 120, 255);
		RenderHelper2D.drawGradientRect(k+2, l+2, k+this.width-2, l+this.height-2, color, color);
	}
	
}
