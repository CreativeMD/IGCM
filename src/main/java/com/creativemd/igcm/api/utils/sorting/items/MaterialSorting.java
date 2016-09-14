package com.creativemd.igcm.api.utils.sorting.items;

import com.creativemd.igcm.api.utils.sorting.SortingItem;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class MaterialSorting extends SortingItem {
	
	public Material material;

	public MaterialSorting(Material material) {
		this.material = material;
	}
	
	@Override
	protected boolean isObject(ItemStack stack) {
		if(stack.getItem() instanceof ItemBlock)
			return Block.getBlockFromItem(stack.getItem()).getMaterial(null) == material;
		return false;
	}

}
