package com.creativemd.igcm.api.segments.advanced;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

import com.creativemd.creativecore.common.utils.stack.InfoBlock;
import com.creativemd.creativecore.common.utils.stack.InfoContainOre;
import com.creativemd.creativecore.common.utils.stack.InfoFuel;
import com.creativemd.creativecore.common.utils.stack.InfoItem;
import com.creativemd.creativecore.common.utils.stack.InfoItemStack;
import com.creativemd.creativecore.common.utils.stack.InfoMaterial;
import com.creativemd.creativecore.common.utils.stack.InfoName;
import com.creativemd.creativecore.common.utils.stack.InfoOre;
import com.creativemd.creativecore.common.utils.stack.InfoStack;
import com.creativemd.creativecore.gui.ContainerControl;
import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.GuiRenderHelper;
import com.creativemd.creativecore.gui.container.SubContainer;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.gui.controls.gui.GuiStateButton;
import com.creativemd.igcm.api.segments.ValueSegment;
import com.creativemd.igcm.container.controls.InfoSlotControl;
import com.creativemd.igcm.utils.SearchUtils;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InfoSegment extends ValueSegment<InfoStack> {

	public InfoSegment(String title, InfoStack defaultValue) {
		super(title, defaultValue);
	}
	
	@SideOnly(Side.CLIENT)
	public GuiLabel label;
	
	public String getLabelText(InfoStack value)
	{
		if(value == null)
			return "";
		else{
			if(value instanceof InfoBlock)
				return "Block: " + ChatFormatting.YELLOW + Block.REGISTRY.getNameForObject(((InfoBlock) value).block).toString();
			else if(value instanceof InfoItem)
				return "Item: " + ChatFormatting.YELLOW + Item.REGISTRY.getNameForObject(((InfoItem) value).item).toString();
			else if(value instanceof InfoItemStack)
				return "ItemStack";
			else if(value instanceof InfoMaterial)
			{
				String name = null;
				
				for (int i = 0; i < Material.class.getDeclaredFields().length; i++) {
					try {
						if(Modifier.isStatic(Material.class.getDeclaredFields()[i].getModifiers()) && Material.class.getDeclaredFields()[i].get(null) == ((InfoMaterial) value).material)
						{
							name = Material.class.getDeclaredFields()[i].getName();
							break;
						}
					} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				
				if(name != null)
					return "Material: " + ChatFormatting.YELLOW + name;				
				return "Material: " + ChatFormatting.YELLOW + "unknown name";
			}
			else if(value instanceof InfoOre)
				return "Ore: " + ChatFormatting.YELLOW + ((InfoOre) value).ore;
			else if(value instanceof InfoContainOre)
				return "Ore (Contains): " + ChatFormatting.YELLOW + ((InfoContainOre) value).ore;
			else if(value instanceof InfoName)
				return "Name (Contains): " + ChatFormatting.YELLOW + ((InfoName) value).name;
			else if(value instanceof InfoFuel)
				return "All fuels";
			else
				return "no description given.";
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y, int maxWidth) {
		ArrayList<GuiControl> controls = super.createGuiControls(gui, x, y, maxWidth);
		label = new GuiLabel(getLabelText(value), 25, y+5);
		controls.add(label);
		return controls;		
	}
	
	@Override
	public ArrayList<ContainerControl> createContainerControls(SubContainer container, int x, int y, int maxWidth) {
		ArrayList<ContainerControl> slots = super.createContainerControls(container, x, y, maxWidth);
		
		InventoryBasic basic = new InventoryBasic(getKey(), false, 1);
		slots.add(new InfoSlotControl(basic, 0, x+5, y+5, value){
			
			@Override
			public void putInfo(InfoStack info)
			{
				super.putInfo(info);
				label.caption = getLabelText(info);
				label.width = GuiRenderHelper.instance.font.getStringWidth(label.caption)+label.getContentOffset()*2;
			}
			
		});
		return slots;
	}
	
	@Override
	public void loadExtra(NBTTagCompound nbt) {
		value = InfoStack.parseNBT(nbt);
	}

	@Override
	public void saveExtra(NBTTagCompound nbt) {
		if(value != null)
			value.writeToNBT(nbt);
	}

	@Override
	public boolean contains(String search)
	{
		if(!super.contains(search))
		{
			if(value != null)
				return SearchUtils.canInfoBeFound(value, search);
			return false;
		}
		return true;
	}

	@Override
	public void saveFromControls() {
		super.saveFromControls();
		value = ((InfoSlotControl) getContainerControl(getKey() + 0)).info;
	}
}
