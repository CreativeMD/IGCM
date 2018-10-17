package com.creativemd.igcm.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

public class TransformInteractor {
	
	public static List modifyList(List list) {
		List<IRecipe> newList = new ArrayList<>();
		for (IRecipe irecipe : (List<IRecipe>) list) {
			if (CraftingManager.REGISTRY.getIDForObject(irecipe) != -1)
				newList.add(irecipe);
		}
		return newList;
	}
	
}
