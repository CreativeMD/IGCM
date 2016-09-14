package com.creativemd.igcm.api.tab;

import java.util.ArrayList;

import org.apache.logging.log4j.Logger;

import com.creativemd.creativecore.client.avatar.Avatar;
import com.creativemd.igcm.api.common.branch.ConfigBranch;
import com.creativemd.igcm.api.core.IGCM;

import net.minecraft.item.ItemStack;

public class ModTab extends Tab
{
	private Logger log = IGCM.logger;
	
	public ModTab(String modname, ItemStack stack)
	{
		super(modname, stack);
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
