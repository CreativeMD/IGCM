package com.creativemd.ingameconfigmanager.api.client.gui;

import javax.vecmath.Vector4d;

import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.gui.controls.gui.custom.GuiInvSelector;
import com.creativemd.creativecore.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.gui.event.gui.GuiControlClickEvent;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SubGuiItemDialog extends SubGui{
	
	public ItemStack stack;
	
	public SubGuiItemDialog()
	{
		super(150, 105);
	}
	
	@Override
	public void createControls() {
		GuiInvSelector selector = new GuiInvSelector("inv", 0, 0, 122, container.player, false);
		controls.add(selector);
		if(stack != null)
			selector.addAndSelectStack(stack);
		
		controls.add(new GuiTextfield("search", "",0, 30, 144, 14));
		
		controls.add(new GuiLabel("StackSize:", 0, 55));		
		GuiTextfield field = new GuiTextfield("stacksize", "1", 110, 55, 30, 14).setNumbersOnly();
		if(stack != null)
			field.text = "" + stack.stackSize;
		controls.add(field);
		
		controls.add(new GuiButton("Cancel", 0, 85, 60) {
			
			@Override
			public void onClicked(int x, int y, int button) {}
		});
		controls.add(new GuiButton("Save", 84, 85, 60) {
			
			@Override
			public void onClicked(int x, int y, int button) {}
		});
	}
	
	@CustomEventSubscribe
	public void onChanged(GuiControlChangedEvent event)
	{
		if(event.source.is("search"))
		{
			GuiInvSelector inv = (GuiInvSelector) get("inv");
			inv.search = ((GuiTextfield)event.source).text.toLowerCase();
			inv.updateItems(container.player);
			inv.closeBox();
		}
	}
	
	@CustomEventSubscribe
	public void onClicked(GuiControlClickEvent event)
	{
		if(event.source.is("Save"))
		{
			stack = ((GuiInvSelector)get("inv")).getStack();
			int stacksize = 1;
			try{
				stacksize = Integer.parseInt(((GuiTextfield)get("stacksize")).text);
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
	
}
