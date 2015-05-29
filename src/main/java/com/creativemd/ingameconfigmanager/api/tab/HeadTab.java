package com.creativemd.ingameconfigmanager.api.tab;

import java.util.ArrayList;

import com.creativemd.ingameconfigmanager.api.client.representative.RepresentativeObject;

public class HeadTab extends Tab{
	
	public HeadTab(String title, RepresentativeObject reprenstive) {
		super(title, reprenstive);
	}

	public ArrayList<SubTab> subTabs = new ArrayList<SubTab>();
	
	public HeadTab addSubTab(SubTab subTab)//void?
	{
		subTabs.add(subTab);
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
