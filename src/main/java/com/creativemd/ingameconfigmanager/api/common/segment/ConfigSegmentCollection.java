package com.creativemd.ingameconfigmanager.api.common.segment;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.logging.log4j.Logger;

import com.creativemd.ingameconfigmanager.api.common.segment.inputType.InputType;
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

public class ConfigSegmentCollection extends ConfigSegment
{
	private HashMap<String, ConfigSegment>	TotalSegmentMap = new HashMap<String, ConfigSegment>();
	private HashMap<String, ConfigSegment>	SegmentMap = new HashMap<String, ConfigSegment>();
	private Logger log = InGameConfigManager.logger;
	
	public ConfigSegmentCollection(String id)
	{
		super(id, SegmentType.Collection, null, null, -1, -1, -1, -1);
	}
	
	public ConfigSegmentCollection addNewConfigSegment(ConfigSegment configSegment)
	{
		String id = configSegment.getID();
		
		if(this.TotalSegmentMap.containsValue(configSegment))
			log.error("ConfigSegment " + id + "is already added!");
		else if(this.TotalSegmentMap.containsKey(id))
			log.error("id " + id + " already occupied!");
		else
		{
			if(configSegment instanceof ConfigSegmentCollection)
			{
				this.SegmentMap.put(id, configSegment);
				this.TotalSegmentMap.put(id, configSegment);
				HashMap segmentMap = ((ConfigSegmentCollection)configSegment).getSegmentMap();
				if(!segmentMap.isEmpty())
				{
					Iterator currentSegment = segmentMap.keySet().iterator();
					while(currentSegment.hasNext())
					{
						String segmentID = (String) currentSegment.next();
						ConfigSegment segment = (ConfigSegment) segmentMap.get(segmentID);
						this.TotalSegmentMap.put(segmentID, configSegment);
					}
				}
			}
			else
			{
				this.TotalSegmentMap.put(id, configSegment);
				this.SegmentMap.put(id, configSegment);
			}
		}
		return this;
	}
	
	
	/** Getters */
	
	public ConfigSegment getSegmentByID(String segmentName)
	{
		return this.SegmentMap.get(segmentName);
	}
	
	public Object getSegmentValue(String segmentName)
	{
		return this.TotalSegmentMap.get(segmentName).getValue();
	}
	
	/** used for internal methods only */
	private HashMap getSegmentMap()
	{
		return this.SegmentMap;
	}

}
