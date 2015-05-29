package com.creativemd.ingameconfigmanager.api.tab;

import java.util.ArrayList;

import com.creativemd.ingameconfigmanager.api.client.representative.RepresentativeObject;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;

public class SubTab extends Tab{
	
	public ArrayList<ConfigBranch> branches = new ArrayList<ConfigBranch>();
	
	public SubTab(String title, RepresentativeObject reprenstive) {
		super(title, reprenstive);
		// TODO Auto-generated constructor stub
	}
	
}
