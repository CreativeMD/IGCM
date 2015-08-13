package com.creativemd.ingameconfigmanager.api.utils.sorting.items;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.creativemd.ingameconfigmanager.api.utils.sorting.SortingItem;

public class BlockSorting extends SortingItem{
	
	public Block block;
	
	public BlockSorting(Block block) {
		this.block = block;
	}
	
	@Override
	protected boolean isObject(ItemStack stack) {
		Item item = stack.getItem();
		if(item instanceof ItemBlock)
		{
			return Block.getBlockFromItem(item) == block;
		}
		return false;
	}

}
