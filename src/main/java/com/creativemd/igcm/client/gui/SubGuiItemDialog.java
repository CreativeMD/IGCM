package com.creativemd.igcm.client.gui;

import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll.SearchSelector;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SubGuiItemDialog extends SubGui {
	
	public ItemStack stack;
	
	public SubGuiItemDialog() {
		super(150, 105);
	}
	
	@Override
	public void createControls() {
		GuiStackSelectorAll selector = new GuiStackSelectorAll("inv", 0, 0, 122, container.player, new GuiStackSelectorAll.CreativeCollector(new GuiStackSelectorAll.SearchSelector()));
		controls.add(selector);
		if (!stack.isEmpty())
			selector.setSelectedForce(stack);
		
		controls.add(new GuiTextfield("search", "", 0, 30, 144, 14));
		
		controls.add(new GuiLabel("StackSize:", 0, 55));
		GuiTextfield field = new GuiTextfield("stacksize", "1", 110, 55, 30, 14).setNumbersOnly();
		int count = 1;
		if (!stack.isEmpty())
			count = stack.getCount();
		field.text = "" + count;
		controls.add(field);
		
		controls.add(new GuiButton("Cancel", 0, 85, 60) {
			
			@Override
			public void onClicked(int x, int y, int button) {
			}
		});
		controls.add(new GuiButton("Save", 84, 85, 60) {
			
			@Override
			public void onClicked(int x, int y, int button) {
			}
		});
	}
	
	@CustomEventSubscribe
	public void onChanged(GuiControlChangedEvent event) {
		if (event.source.is("search")) {
			GuiStackSelectorAll inv = (GuiStackSelectorAll) get("inv");
			((SearchSelector) inv.collector.selector).search = ((GuiTextfield) event.source).text.toLowerCase();
			inv.updateCollectedStacks();
			inv.closeBox();
		}
	}
	
	@CustomEventSubscribe
	public void onClicked(GuiControlClickEvent event) {
		if (event.source.is("Save")) {
			stack = ((GuiStackSelectorAll) get("inv")).getSelected();
			int stacksize = 1;
			try {
				stacksize = Integer.parseInt(((GuiTextfield) get("stacksize")).text);
			} catch (Exception e) {
				stacksize = 1;
			}
			if (stack != null) {
				stack = stack.copy();
				stack.setCount(stacksize);
			}
			
			closeLayer(new NBTTagCompound());
		}
		if (event.source.is("Cancel")) {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("canceled", true);
			closeLayer(nbt);
		}
	}
	
}
