package com.creativemd.ingameconfigmanager.api.utils.sorting;

import net.minecraft.item.ItemStack;

public abstract class SortingItem {
	
	protected abstract boolean isObject(ItemStack stack);
	
	/**If stackSize is below 1 it will be ignored*/
	public int stackSize = -1;
	
	public SortingItem setStackSize(int stackSize)
	{
		this.stackSize = stackSize;
		return this;
	}
	
	public boolean isObjectEqual(ItemStack stack)
	{
		if(isObject(stack))
		{
			return stack.stackSize >= stackSize || stackSize <= 0;
		}
		return false;
	}
}
