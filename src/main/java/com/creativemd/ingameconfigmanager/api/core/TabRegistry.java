package com.creativemd.ingameconfigmanager.api.core;

import java.util.ArrayList;

import com.creativemd.ingameconfigmanager.api.tab.ModTab;

public class TabRegistry {
	
	private static ArrayList<ModTab> tabs = new ArrayList<ModTab>();
	
	public static ModTab registerModTab(ModTab tab)
	{
		tabs.add(tab);
		tab.setID(tabs.size());
		return tab;
	}
	
	public ModTab getTabByIndex(int index)
	{
		return tabs.get(index);
	}
	
	public static ArrayList<ModTab> getTabs()
	{
		return tabs;
	}
	
}
