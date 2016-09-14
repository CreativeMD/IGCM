package com.creativemd.ingameconfigmanager.api.core;

import java.util.ArrayList;
import java.util.HashMap;

import com.creativemd.ingameconfigmanager.api.tab.ModTab;

public class TabRegistry {
	
	public static HashMap<String, ModTab> tabs = new HashMap<>();
	
	public static ModTab registerModTab(ModTab tab)
	{
		tab.setID(tabs.size());
		tabs.put(tab.title, tab);
		return tab;
	}
	
	public static ModTab getTabByID(String id)
	{
		return tabs.get(id);
	}
	
}
