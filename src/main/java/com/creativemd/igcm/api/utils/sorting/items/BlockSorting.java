package com.creativemd.igcm.api.utils.sorting.items;

import com.creativemd.igcm.api.utils.sorting.SortingItem;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

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
