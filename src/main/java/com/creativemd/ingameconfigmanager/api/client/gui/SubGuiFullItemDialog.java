package com.creativemd.ingameconfigmanager.api.client.gui;

import java.util.ArrayList;
import java.util.Arrays;

import javax.vecmath.Vector4d;

import com.creativemd.creativecore.client.avatar.AvatarItemStack;
import com.creativemd.creativecore.common.utils.ColorUtils;
import com.creativemd.creativecore.common.utils.stack.StackInfo;
import com.creativemd.creativecore.common.utils.stack.StackInfoBlock;
import com.creativemd.creativecore.common.utils.stack.StackInfoFuel;
import com.creativemd.creativecore.common.utils.stack.StackInfoItem;
import com.creativemd.creativecore.common.utils.stack.StackInfoItemStack;
import com.creativemd.creativecore.common.utils.stack.StackInfoMaterial;
import com.creativemd.creativecore.common.utils.stack.StackInfoOre;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.gui.GuiAvatarLabelClickable;
import com.creativemd.creativecore.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.gui.controls.gui.GuiStateButton;
import com.creativemd.creativecore.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.gui.controls.gui.custom.GuiInvSelector;
import com.creativemd.creativecore.gui.controls.gui.custom.GuiStackSelector;
import com.creativemd.creativecore.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.gui.event.gui.GuiControlClickEvent;
import com.creativemd.creativecore.gui.event.gui.GuiToolTipEvent;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class SubGuiFullItemDialog extends SubGui{
	
	public static ArrayList<StackInfo> latest = new ArrayList<StackInfo>();
	
	public StackInfo info;
	
	public boolean supportStackSize;
	
	public SubGuiFullItemDialog(boolean supportStackSize)
	{
		super(150, 230);
		this.supportStackSize = supportStackSize;
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
		//lines.add("Latest");
		
		GuiComboBox box = new GuiComboBox("type", 0, 0, 144, lines);
		int index = lines.indexOf(selected);
		box.caption = selected;
		box.index = index;
		controls.add(box);
		
		GuiScrollBox scroll = new GuiScrollBox("latest", 0, 155, 144, 65);
		int latestPerRow = 4;
		for (int i = 0; i < latest.size(); i++) {
			int row = i/latestPerRow;
			int cell = i-(row*latestPerRow);
			
			GuiAvatarLabelClickable avatar = new GuiAvatarLabelClickable("" + i, cell*32, row*18, ColorUtils.WHITE, new AvatarItemStack(latest.get(i).getItemStack())) {
				
				@Override
				public void onClicked(int x, int y, int button) {
					info = latest.get(Integer.parseInt(name));
					closeLayer(new NBTTagCompound());
				}
			};
			scroll.addControl(avatar);
		}
		controls.add(scroll);
		
		
		switch(index)
		{
		case 0:
			GuiInvSelector selector = new GuiInvSelector("inv", 0, 30, 122, container.player, false);
			controls.add(selector);
			controls.add(new GuiTextfield("search", "", 0, 57, 144, 14));
			
			controls.add(new GuiLabel("guilabel1", 0, 80));
			controls.add(new GuiLabel("guilabel2", 0, 90));
			
			GuiStateButton damage = new GuiStateButton("damage", 0, 0, 106, 70, 14, "Damage: Off", "Damage: On");
			controls.add(damage);
			GuiStateButton nbt = new GuiStateButton("nbt", 0, 80, 106, 60, 14, "NBT: Off", "NBT: On");
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
			GuiComboBox ore = new GuiComboBox("ore", 0, 30, 144, ores);
			controls.add(ore);
			controls.add(new GuiTextfield("search", "", 0, 57, 144, 14));
			
			if(info instanceof StackInfoOre)
				ore.caption = ((StackInfoOre) info).ore;
			break;
		case 2:
			selector = new GuiInvSelector("inv", 0, 30, 122, container.player, true);
			controls.add(selector);
			if(info instanceof StackInfoMaterial)
				selector.addAndSelectStack(info.getItemStack());
			break;
		case 3:
			controls.add(new GuiLabel("Nothing to select", 5, 30));
			break;
		/*case 4:
			selector = new GuiStackSelector("stack", 0, 30, 122, container.player, false);
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
			break;*/
		}
		
		if(supportStackSize)
		{
			controls.add(new GuiLabel("StackSize:", 5, 132));
			GuiTextfield field = new GuiTextfield("stacksize", "1", 110, 127, 30, 20).setNumbersOnly();
			if(info != null)
				field.text = "" + info.stackSize;
			controls.add(field);
		}
		
		controls.add(new GuiButton("Cancel", 0, 130, 41) {
			
			@Override
			public void onClicked(int x, int y, int button) {}
		});
		controls.add(new GuiButton("Remove", 50, 130, 41) {
			
			@Override
			public void onClicked(int x, int y, int button) {}
		});
		controls.add(new GuiButton("Save", 100, 130, 41) {
			
			@Override
			public void onClicked(int x, int y, int button) {}
		});
	}
	
	@CustomEventSubscribe
	public void onClicked(GuiControlClickEvent event)
	{
		if(event.source.is("Save"))
		{
			int index = ((GuiComboBox)controls.get(0)).index;
			int stacksize = 0;
			try{
				if(supportStackSize)
					stacksize = Integer.parseInt(((GuiTextfield)get("stacksize")).text);
				else
					stacksize = 1;
			}catch (Exception e){
				stacksize = 1;
			}
			StackInfo info = null;
			switch(index)
			{
			case 0:
				ItemStack stack = ((GuiInvSelector)get("inv")).getStack();
				if(stack != null)
				{
					boolean damage = ((GuiStateButton)get("damage")).getState() == 1;
					boolean nbt = ((GuiStateButton)get("nbt")).getState() == 1;
					if(damage)
					{
						info = new StackInfoItemStack(stack, nbt, stacksize);
					}else{
						if(Block.getBlockFromItem(stack.getItem()) != null)
							info = new StackInfoBlock(Block.getBlockFromItem(stack.getItem()), stacksize);
						else
							info = new StackInfoItem(stack.getItem(), stacksize);
					}
				}
				break;
			case 1:
				String ore = ((GuiComboBox) get("ore")).caption;
				if(!ore.equals(""))
					info = new StackInfoOre(ore, stacksize);
				break;
			case 2:
				ItemStack blockStack = ((GuiInvSelector)get("inv")).getStack();
				if(blockStack != null)
				{
					Block block = Block.getBlockFromItem(blockStack.getItem());
					if(!(block instanceof BlockAir))
						info = new StackInfoMaterial(block.getMaterial(null), stacksize);
				}
				break;
			case 3:
				info = new StackInfoFuel(stacksize);
				break;
			/*case 4:
				int stackIndex = ((GuiInvSelector)get("stack")).index;
				if(stackIndex >= 0 && stackIndex < latest.size())
					info = latest.get(stackIndex).copy();
				break;*/
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
	public void onChanged(GuiControlChangedEvent event)
	{
		if(event.source.is("type"))
		{
			createControls();
			refreshControls();
		}
		if(event.source.is("search"))
		{
			int index = ((GuiComboBox)controls.get(0)).index;
			if(index == 1)
			{
				String search = ((GuiTextfield)event.source).text;
				String[] oreNames = OreDictionary.getOreNames();
				ArrayList<String> ores = new ArrayList<String>();
				for (int i = 0; i < oreNames.length; i++) {
					if(oreNames[i].toLowerCase().contains(search.toLowerCase()))
						ores.add(oreNames[i]);
				}
				GuiComboBox comboBox = (GuiComboBox) get("ore");
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
				GuiInvSelector inv = (GuiInvSelector) get("inv");
				inv.search = ((GuiTextfield)event.source).text.toLowerCase();
				inv.updateItems(container.player);
				inv.closeBox();
			}
		}
	}

	@Override
	public void onTick() {
		int index = ((GuiComboBox)controls.get(0)).lines.indexOf(((GuiComboBox)controls.get(0)).caption);
		
		switch(index)
		{
		case 0:
			GuiInvSelector selector = (GuiInvSelector) get("inv");
			if(selector != null)
			{
				int indexStack = selector.index;
				if(indexStack != -1)
				{
					ItemStack stack = selector.stacks.get(indexStack);
					((GuiLabel)get("guilabel1")).caption = "damage:" + stack.getItemDamage();
					((GuiLabel)get("guilabel2")).caption = "nbt:" + (stack.hasTagCompound() ? stack.getTagCompound().toString() : "null");
				}else{
					((GuiLabel)get("guilabel1")).caption = "";
					((GuiLabel)get("guilabel2")).caption = "";
				}
			}
			break;
		}
			
	}

}
