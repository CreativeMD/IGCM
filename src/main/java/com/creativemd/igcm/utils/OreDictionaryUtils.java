package com.creativemd.igcm.utils;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryUtils {
	
	public static boolean isOreOf(String ore, Block block) {
		return isOreOf(ore, Item.getItemFromBlock(block));
	}
	
	public static boolean isOreOf(String ore, Item item) {
		List<ItemStack> stacks = OreDictionary.getOres(ore);
		for (int i = 0; i < stacks.size(); i++) {
			if (stacks.get(i).getItem() == item)
				return true;
		}
		return false;
	}
	
	public static boolean isOreOf(String ore, ItemStack stack) {
		List<ItemStack> stacks = OreDictionary.getOres(ore);
		for (int i = 0; i < stacks.size(); i++) {
			if (stacks.get(i).isItemEqual(stack))
				return true;
		}
		return false;
	}
	
}