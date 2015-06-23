package com.creativemd.ingameconfigmanager.api.tab.core;

import java.util.ArrayList;

import com.creativemd.ingameconfigmanager.api.tab.HeadTab;

public class TabRegistry {
	
	private static ArrayList<HeadTab> tabs = new ArrayList<HeadTab>();
	
	public static void registerHeadTab(HeadTab tab)
	{
		tabs.add(tab);
		tab.setID(tabs.size());
	}
	
	public HeadTab getTabByIndex(int index)
	{
		return tabs.get(index);
	}
	
	public static ArrayList<HeadTab> getTabs()
	{
		return (ArrayList<HeadTab>) tabs.clone();
	}
	

	
}
