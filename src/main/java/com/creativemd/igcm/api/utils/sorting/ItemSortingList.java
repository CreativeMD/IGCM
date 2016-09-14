package com.creativemd.igcm.api.utils.sorting;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class ItemSortingList extends ArrayList<SortingItem>{
	
	public boolean isWhiteList = true;
	
	public ItemSortingList setBlackList()
	{
		isWhiteList = false;
		return this;
	}
	
	public boolean isObjectValid(ItemStack stack)
	{
		if(stack != null && stack.getItem() != null)
		{
			for (int i = 0; i < size(); i++) {
				if(get(i).isObject(stack))
				{
					return isWhiteList;
				}
			}
		}
		return !isWhiteList;
	}
	
}
