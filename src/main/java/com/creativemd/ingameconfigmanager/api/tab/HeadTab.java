package com.creativemd.ingameconfigmanager.api.tab;

import java.util.ArrayList;

import org.apache.logging.log4j.Logger;

import com.creativemd.ingameconfigmanager.api.client.representative.RepresentativeObject;
import com.creativemd.ingameconfigmanager.core.InGameConfigManager;

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
