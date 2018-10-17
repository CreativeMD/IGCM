package com.creativemd.igcm.block;

import com.creativemd.creativecore.common.recipe.GridRecipe;
import com.creativemd.creativecore.common.utils.stack.InfoStack;

import net.minecraft.item.ItemStack;

public class AdvancedGridRecipe extends GridRecipe {
	
	public int duration;
	
	public AdvancedGridRecipe(ItemStack[] output, int width, int height, InfoStack[] input, int duration) {
		super(output, width, height, input);
		this.duration = duration;
	}
	
}
