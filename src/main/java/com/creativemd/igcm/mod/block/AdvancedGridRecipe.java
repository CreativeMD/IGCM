package com.creativemd.igcm.mod.block;

import com.creativemd.creativecore.common.recipe.GridRecipe;
import com.creativemd.creativecore.common.utils.stack.StackInfo;

import net.minecraft.item.ItemStack;

public class AdvancedGridRecipe extends GridRecipe{
	
	public int duration;
	
	public AdvancedGridRecipe(ItemStack[] output, int width, int height, StackInfo[] input, int duration) {
		super(output, width, height, input);
		this.duration = duration;
	}

}
