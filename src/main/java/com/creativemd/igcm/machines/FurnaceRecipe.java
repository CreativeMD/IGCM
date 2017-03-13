package com.creativemd.igcm.machines;

import com.creativemd.creativecore.common.recipe.Recipe;

import net.minecraft.item.ItemStack;

public class FurnaceRecipe extends Recipe{
	
	public float experience;
	
	public FurnaceRecipe(ItemStack output, Object input, float experience) {
		super(output, input);
		this.experience = experience;
	}

}
