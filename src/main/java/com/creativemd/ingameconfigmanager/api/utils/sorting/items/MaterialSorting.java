package com.creativemd.ingameconfigmanager.api.utils.sorting.items;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.creativemd.ingameconfigmanager.api.utils.sorting.SortingItem;

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
