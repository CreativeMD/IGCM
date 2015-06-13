package com.creativemd.ingameconfigmanager.api.tab.core;

import java.util.ArrayList;

import com.creativemd.ingameconfigmanager.api.tab.HeadTab;

/**
 * Copyright 2015 CreativeMD & N247S
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

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
