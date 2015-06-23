package com.creativemd.ingameconfigmanager.api.common.branch;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;

import com.creativemd.ingameconfigmanager.api.common.segment.ConfigSegment;
import com.creativemd.ingameconfigmanager.api.tab.core.InGameConfigManager;

public class ConfigSegmentCollection
{
	private HashMap<String, ConfigSegment>	TotalSegmentMap = new HashMap<String, ConfigSegment>();
	private Logger log = InGameConfigManager.logger;
	
	public ConfigSegmentCollection(ArrayList<ConfigSegment> segment)
	{
		for (int i = 0; i < segment.size(); i++) {
			addNewConfigSegment(segment.get(i));
		}
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
			this.TotalSegmentMap.put(id, configSegment);
		}
		return this;
	}
	
	
	/** Getters */
	
	public ConfigSegment getSegmentByID(String segmentName)
	{
		return this.TotalSegmentMap.get(segmentName);
	}
	
	public Object getSegmentValue(String segmentName)
	{
		return this.TotalSegmentMap.get(segmentName).value;
	}
	
	/** used for internal methods only */
	private HashMap getSegmentMap()
	{
		return this.TotalSegmentMap;
	}

}
