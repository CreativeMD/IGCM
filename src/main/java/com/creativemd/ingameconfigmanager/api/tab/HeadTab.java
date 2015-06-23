package com.creativemd.ingameconfigmanager.api.tab;

import java.util.ArrayList;

import org.apache.logging.log4j.Logger;

import com.creativemd.ingameconfigmanager.api.client.representative.RepresentativeObject;
import com.creativemd.ingameconfigmanager.api.tab.core.InGameConfigManager;

public class HeadTab extends Tab
{
	private Logger log = InGameConfigManager.logger;
	
	public HeadTab(String title, RepresentativeObject reprenstive)
	{
		super(title, reprenstive);
	}

	public ArrayList<SubTab> subTabs = new ArrayList<SubTab>();
	
	public HeadTab addSubTab(SubTab subTab)
	{
		if(!subTabs.contains(subTab))
			subTabs.add(subTab);
		else log.error("SubTab " + subTab.title + " is already added!");
		return this;
	}
	
	private int id = -1;
	
	/**Should never be called by a modder!**/
	public void setID(int id)//protected?
	{
		if(this.id != -1)
			this.id = id;
	}
}
