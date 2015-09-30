package com.creativemd.ingameconfigmanager.api.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import javax.vecmath.Vector4d;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import com.creativemd.creativecore.client.rendering.RenderHelper2D;
import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.common.gui.controls.GuiButton;
import com.creativemd.creativecore.common.gui.controls.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.GuiControl;
import com.creativemd.creativecore.common.gui.controls.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.GuiStateButton;
import com.creativemd.creativecore.common.gui.controls.GuiTextfield;
import com.creativemd.creativecore.common.gui.event.ControlChangedEvent;
import com.creativemd.creativecore.common.gui.event.ControlClickEvent;
import com.creativemd.creativecore.common.gui.event.GuiToolTipEvent;
import com.creativemd.creativecore.common.utils.stack.StackInfo;
import com.creativemd.creativecore.common.utils.stack.StackInfoBlock;
import com.creativemd.creativecore.common.utils.stack.StackInfoFuel;
import com.creativemd.creativecore.common.utils.stack.StackInfoItem;
import com.creativemd.creativecore.common.utils.stack.StackInfoItemStack;
import com.creativemd.creativecore.common.utils.stack.StackInfoMaterial;
import com.creativemd.creativecore.common.utils.stack.StackInfoOre;
import com.creativemd.ingameconfigmanager.api.common.container.controls.GuiInvSelector;
import com.creativemd.ingameconfigmanager.api.common.container.controls.GuiStackSelector;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

public class SubGuiFullItemDialog extends SubGui{
	
	public static ArrayList<StackInfo> latest = new ArrayList<StackInfo>();
	
	public StackInfo info;
	
	public SubGuiFullItemDialog()
	{
		super(150, 175);
	}

	@Override
	public void createControls() {
		String selected = "Default";
		if(controls.size() > 0)
		{
			selected = ((GuiComboBox)controls.get(0)).caption;
		}else if(info != null){
			if(info instanceof StackInfoBlock || info instanceof StackInfoItem || info instanceof StackInfoItemStack)
				selected = "Default";
			if(info instanceof StackInfoOre)
				selected = "Ore";
			if(info instanceof StackInfoMaterial)
				selected = "Material";
			if(info instanceof StackInfoFuel)
				selected = "Fuel";
		}
		controls.clear();
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("Default");
		lines.add("Ore");
		lines.add("Material");
		lines.add("Fuel");
		lines.add("Latest");
		
		GuiComboBox box = new GuiComboBox("type", 5, 5, 140, lines);
		box.caption = selected;
		controls.add(box);
		int index = lines.indexOf(selected);
		
		switch(index)
		{
		case 0:
			GuiInvSelector selector = new GuiInvSelector("inv", 5, 30, 140, container.player, false);
			controls.add(selector);
			controls.add(new GuiTextfield("search", "", 5, 55, 140, 20));
			
			controls.add(new GuiLabel("guilabel1", 5, 80));
			controls.add(new GuiLabel("guilabel2", 5, 90));
			
			GuiStateButton damage = new GuiStateButton("damage", 0, 5, 102, 70, 20, "Damage: Off", "Damage: On");
			controls.add(damage);
			GuiStateButton nbt = new GuiStateButton("nbt", 0, 85, 102, 60, 20, "NBT: Off", "NBT: On");
			controls.add(nbt);
			
			if(info instanceof StackInfoBlock || info instanceof StackInfoItem || info instanceof StackInfoItemStack)
			{
				selector.addAndSelectStack(info.getItemStack().copy());
				if(info instanceof StackInfoItemStack)
				{
					damage.nextState();
					if(((StackInfoItemStack) info).needNBT)
						nbt.nextState();
				}
			}
			break;
		case 1:
			ArrayList<String> ores = new ArrayList<String>(Arrays.asList(OreDictionary.getOreNames()));
			GuiComboBox ore = new GuiComboBox("ore", 5, 30, 140, ores);
			controls.add(ore);
			controls.add(new GuiTextfield("search", "", 5, 55, 140, 20));
			
			if(info instanceof StackInfoOre)
				ore.caption = ((StackInfoOre) info).ore;
			break;
		case 2:
			selector = new GuiInvSelector("inv", 5, 30, 140, container.player, true);
			controls.add(selector);
			if(info instanceof StackInfoMaterial)
				selector.addAndSelectStack(info.getItemStack());
			break;
		case 3:
			controls.add(new GuiLabel("Nothing to select", 5, 30));
			break;
		case 4:
			selector = new GuiStackSelector("stack", 5, 30, 140, container.player, false);
			selector.stacks.clear();
			selector.lines.clear();
			for (int i = 0; i < latest.size(); i++) {
				selector.addAndSelectStack(latest.get(i).getItemStack());
			}
			if(selector.lines.size() > 0)
				selector.caption = selector.lines.get(0);
			else
				selector.caption = "";
			controls.add(selector);
			break;
		}
		
		controls.add(new GuiLabel("StackSize:", 5, 132));
		GuiTextfield field = new GuiTextfield("stacksize", "1", 110, 127, 30, 20).setNumbersOnly();
		if(info != null)
			field.text = "" + info.stackSize;
		controls.add(field);
		
		controls.add(new GuiButton("Cancel", 5, 150, 45, 20));
		controls.add(new GuiButton("Remove", 52, 150, 45, 20));
		controls.add(new GuiButton("Save", 100, 150, 45, 20));
	}
	
	@CustomEventSubscribe
	public void onClicked(ControlClickEvent event)
	{
		if(event.source.is("Save"))
		{
			int index = ((GuiComboBox)controls.get(0)).lines.indexOf(((GuiComboBox)controls.get(0)).caption);
			int stacksize = 0;
			try{
				stacksize = Integer.parseInt(((GuiTextfield)getControl("stacksize")).text);
			}catch (Exception e){
				stacksize = 0;
			}
			StackInfo info = null;
			switch(index)
			{
			case 0:
				ItemStack stack = ((GuiInvSelector)getControl("inv")).getStack();
				if(stack != null)
				{
					boolean damage = ((GuiStateButton)getControl("damage")).getState() == 1;
					boolean nbt = ((GuiStateButton)getControl("nbt")).getState() == 1;
					if(damage)
					{
						info = new StackInfoItemStack(stack, nbt, stacksize);
					}else{
						if(!(Block.getBlockFromItem(stack.getItem()) instanceof BlockAir))
							info = new StackInfoBlock(Block.getBlockFromItem(stack.getItem()), stacksize);
						else
							info = new StackInfoItem(stack.getItem(), stacksize);
					}
				}
				break;
			case 1:
				String ore = ((GuiComboBox) getControl("ore")).caption;
				if(!ore.equals(""))
					info = new StackInfoOre(ore, stacksize);
				break;
			case 2:
				ItemStack blockStack = ((GuiInvSelector)getControl("inv")).getStack();
				if(blockStack != null)
				{
					Block block = Block.getBlockFromItem(blockStack.getItem());
					if(!(block instanceof BlockAir))
						info = new StackInfoMaterial(block.getMaterial(), stacksize);
				}
				break;
			case 3:
				info = new StackInfoFuel(stacksize);
				break;
			case 4:
				int stackIndex = ((GuiInvSelector)getControl("stack")).getIndex();
				if(stackIndex >= 0 && stackIndex < latest.size())
					info = latest.get(stackIndex).copy();
				break;
			}
			if(info != null)
			{
				this.info = info;
				if(!latest.contains(info))
					latest.add(0, info.copy());
				closeLayer(new NBTTagCompound());
			}
		}
		if(event.source.is("Remove"))
		{
			this.info = null;
			closeLayer(new NBTTagCompound());
		}
		if(event.source.is("Cancel"))
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("canceled", true);
			closeLayer(nbt);
		}
	}
	
	@CustomEventSubscribe
	public void onToolTip(GuiToolTipEvent event)
	{
		if(event.source.is("stacksize"))
		{
			event.tooltip.add("0: no consumption");
			event.tooltip.add("1: normal");
		}
	}
	
	@CustomEventSubscribe
	public void onChanged(ControlChangedEvent event)
	{
		if(event.source.is("type"))
		{
			createControls();
			refreshControls();
		}
		if(event.source.is("search"))
		{
			int index = ((GuiComboBox)controls.get(0)).lines.indexOf(((GuiComboBox)controls.get(0)).caption);
			if(index == 1)
			{
				String search = ((GuiTextfield)event.source).text;
				String[] oreNames = OreDictionary.getOreNames();
				ArrayList<String> ores = new ArrayList<String>();
				for (int i = 0; i < oreNames.length; i++) {
					if(oreNames[i].toLowerCase().contains(search.toLowerCase()))
						ores.add(oreNames[i]);
				}
				GuiComboBox comboBox = (GuiComboBox) getControl("ore");
				if(comboBox != null)
				{
					comboBox.lines = ores;
					if(!ores.contains(comboBox.caption))
					{
						if(ores.size() > 0)
							comboBox.caption = ores.get(0);
						else
							comboBox.caption = "";
					}
				}
			}else if(index == 0){
				GuiInvSelector inv = (GuiInvSelector) getControl("inv");
				inv.search = ((GuiTextfield)event.source).text.toLowerCase();
				inv.updateItems(container.player);
				inv.closeBox();
			}
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

	@Override
	public void drawOverlay(FontRenderer fontRenderer) {
		int index = ((GuiComboBox)controls.get(0)).lines.indexOf(((GuiComboBox)controls.get(0)).caption);
		
		switch(index)
		{
		case 0:
			GuiInvSelector selector = (GuiInvSelector) getControl("inv");
			if(selector != null)
			{
				int indexStack = selector.lines.indexOf(selector.caption);
				if(indexStack != -1)
				{
					ItemStack stack = selector.stacks.get(indexStack);
					((GuiLabel)getControl("guilabel1")).title = "damage:" + stack.getItemDamage();
					((GuiLabel)getControl("guilabel2")).title = "nbt:" + (stack.stackTagCompound != null ? stack.stackTagCompound.toString() : "null");
				}else{
					((GuiLabel)getControl("guilabel1")).title = "";
					((GuiLabel)getControl("guilabel2")).title = "";
				}
			}
			break;
		}
			
	}

}
