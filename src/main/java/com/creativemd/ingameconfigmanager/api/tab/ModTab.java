package com.creativemd.ingameconfigmanager.api.tab;

import java.util.ArrayList;

import org.apache.logging.log4j.Logger;

import com.creativemd.creativecore.client.avatar.Avatar;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;
import com.creativemd.ingameconfigmanager.api.core.InGameConfigManager;

public class ModTab extends Tab
{
	private Logger log = InGameConfigManager.logger;
	
	public ModTab(String modname, Avatar avatar)
	{
		super(modname, avatar);
	}
	
	public ArrayList<ConfigBranch> branches = new ArrayList<ConfigBranch>();
	
	public void addBranch(ConfigBranch branch)
	{
		branches.add(branch);
		branch.tab = this;
	}
	
	private int id = -1;
	
	/**Should never be called by a modder!**/
	public void setID(int id)
	{
		if(this.id == -1)
			this.id = id;
	}
	
	public int getID()
	{
		return id;
	}
}
