package com.creativemd.igcm.api.tab;

import java.util.ArrayList;

import com.creativemd.creativecore.client.avatar.Avatar;
import com.creativemd.igcm.api.common.branch.ConfigBranch;

import net.minecraft.item.ItemStack;

/**Unused!**/
public class SubTab extends Tab{
	
	public ArrayList<ConfigBranch> branches = new ArrayList<ConfigBranch>();
	
	public SubTab(String title, ItemStack stack) {
		super(title, stack);
	}
	
	public void addBranch(ConfigBranch branch)
	{
		branches.add(branch);
	}
	
}