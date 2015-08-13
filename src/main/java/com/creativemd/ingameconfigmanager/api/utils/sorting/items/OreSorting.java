package com.creativemd.ingameconfigmanager.api.utils.sorting.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.creativemd.ingameconfigmanager.api.utils.sorting.SortingItem;

public class OreSorting extends SortingItem {
	
	public boolean canContain;
	public String ore;
	
	public OreSorting(String ore) {
		this(ore, false);
	}
	
	public OreSorting(String ore, boolean canContain) {
		this.ore = ore.toLowerCase();
		this.canContain = canContain;
	}
	
	@Override
	protected boolean isObject(ItemStack stack) {
		int[] ores = OreDictionary.getOreIDs(stack);
		for (int i = 0; i < ores.length; i++) {
			if(canContain)
				if(OreDictionary.getOreName(ores[i]).toLowerCase().contains(ore))
					return true;
			else
				if(OreDictionary.getOreName(ores[i]).toLowerCase().equals(ore))
					return true;
		}
		return false;
	}

}
