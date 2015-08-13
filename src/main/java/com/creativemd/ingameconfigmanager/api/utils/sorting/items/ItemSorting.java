package com.creativemd.ingameconfigmanager.api.utils.sorting.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.creativemd.ingameconfigmanager.api.utils.sorting.SortingItem;

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
