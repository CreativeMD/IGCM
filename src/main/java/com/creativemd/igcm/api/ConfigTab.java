package com.creativemd.igcm.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ConfigTab extends ConfigGroupElement {
	
	public static final ConfigTab root = new ConfigTab("root", ItemStack.EMPTY);
	
	public static ConfigSegment getSegmentByPath(String path)
	{
		if(path.equals("root"))
			return root;
		if(path.startsWith("root."))
			return root.getChildByPath(path.substring("root.".length()));
		return null;
	}
	
	public ConfigTab(String title, ItemStack avatar) {
		super(title, avatar);
	}
	
	public ConfigSegment getChildByPath(String path)
	{
		String[] patterns = path.split("\\.");
		ConfigSegment currentElement = this;
		for (int i = 0; i < patterns.length; i++) {
			if(currentElement instanceof ConfigGroupElement)
			{
				currentElement = ((ConfigGroupElement) currentElement).getChildByKey(patterns[i]);
				if(currentElement == null)
					return null;
			}else
				return null;
		}
		return currentElement;
	}

	@Override
	public void loadExtra(NBTTagCompound nbt) {
		
	}

	@Override
	public void saveExtra(NBTTagCompound nbt) {
		
	}

	@Override
	public void saveFromControls() {
		
	}
	
}
