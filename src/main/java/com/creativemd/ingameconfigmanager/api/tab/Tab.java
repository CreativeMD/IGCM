package com.creativemd.ingameconfigmanager.api.tab;

import com.creativemd.ingameconfigmanager.api.client.representative.RepresentativeObject;

public abstract class Tab {
	
	public String title;
	
	public RepresentativeObject reprenstive;
	
	public Tab(String title, RepresentativeObject reprenstive)
	{
		this.title = title;
		this.reprenstive = reprenstive;
	}
	
}