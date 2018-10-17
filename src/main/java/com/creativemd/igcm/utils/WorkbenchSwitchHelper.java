package com.creativemd.igcm.utils;

import java.util.ArrayList;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class WorkbenchSwitchHelper {
	
	public static ArrayList<ItemStack> findMatchingRecipe(InventoryCrafting par1InventoryCrafting, World par2World) {
		int i = 0;
		ItemStack itemstack = null;
		ItemStack itemstack1 = null;
		ArrayList<ItemStack> result = new ArrayList<ItemStack>();
		int j;
		
		for (j = 0; j < par1InventoryCrafting.getSizeInventory(); ++j) {
			ItemStack itemstack2 = par1InventoryCrafting.getStackInSlot(j);
			
			if (itemstack2 != null) {
				if (i == 0) {
					itemstack = itemstack2;
				}
				
				if (i == 1) {
					itemstack1 = itemstack2;
				}
				
				++i;
			}
		}
		
		if (i == 2 && itemstack.getItem() == itemstack1.getItem() && itemstack.getCount() == 1 && itemstack1.getCount() == 1 && itemstack.getItem().isRepairable()) {
			Item item = itemstack.getItem();
			int j1 = item.getMaxDamage(itemstack) - itemstack.getItemDamage();
			int k = item.getMaxDamage(itemstack1) - itemstack1.getItemDamage();
			int l = j1 + k + item.getMaxDamage(itemstack) * 5 / 100;
			int i1 = item.getMaxDamage(itemstack1) - l;
			
			if (i1 < 0) {
				i1 = 0;
			}
			
			result.add(new ItemStack(itemstack.getItem(), 1, i1));
		} else {
			//List recipes = CraftingManager.getInstance().getRecipeList();
			for (IRecipe irecipe : CraftingManager.REGISTRY) {
				if (irecipe.matches(par1InventoryCrafting, par2World)) {
					ItemStack output = irecipe.getCraftingResult(par1InventoryCrafting);
					if (!output.isEmpty())
						result.add(output);
				}
			}
		}
		return result;
	}
}