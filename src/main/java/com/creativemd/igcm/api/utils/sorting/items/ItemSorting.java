package com.creativemd.igcm.api.utils.sorting.items;

import com.creativemd.igcm.api.utils.sorting.SortingItem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemSorting extends SortingItem{
	
	public Item item;
	
	public ItemSorting(Item item) {
		this.item = item;
	}
	
	@Override
	protected boolean isObject(ItemStack stack) {
		return stack.getItem() == item;
	}

}
