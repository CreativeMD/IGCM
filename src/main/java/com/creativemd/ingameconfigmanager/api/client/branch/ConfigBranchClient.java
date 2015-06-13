package com.creativemd.ingameconfigmanager.api.client.branch;

import java.util.ArrayList;

import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;
import com.creativemd.ingameconfigmanager.api.common.segment.ConfigSegment;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ConfigBranchClient extends ConfigBranch{
	
	public ConfigBranchClient(String name) {
		super(name);
	}
	
	@Override
	public ArrayList<ConfigSegment> getServerSegments() {
		return new ArrayList<ConfigSegment>();
	}
	
	@Override
	public boolean needClientConfig()
	{
		return true;
	}
	
	@Override
	public boolean needPacket() {
		return false;
	}

}
