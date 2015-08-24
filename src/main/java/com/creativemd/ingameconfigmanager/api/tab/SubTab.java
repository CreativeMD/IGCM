package com.creativemd.ingameconfigmanager.api.tab;

import java.util.ArrayList;

import com.creativemd.creativecore.client.avatar.Avatar;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;

/**Unused!**/
public class SubTab extends Tab{
	
	public ArrayList<ConfigBranch> branches = new ArrayList<ConfigBranch>();
	
	public SubTab(String title, Avatar avatar) {
		super(title, avatar);
	}
	
	public void addBranch(ConfigBranch branch)
	{
		branches.add(branch);
	}
	
}