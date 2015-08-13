package com.creativemd.ingameconfigmanager.api.tab;

import com.creativemd.creativecore.client.avatar.Avatar;

public abstract class Tab {
	
	public String title;
	
	public Avatar avatar;
	
	public Tab(String title, Avatar avatar)
	{
		this.title = title;
		this.avatar = avatar;
	}
	
}