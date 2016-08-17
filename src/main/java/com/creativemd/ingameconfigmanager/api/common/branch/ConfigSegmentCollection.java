package com.creativemd.ingameconfigmanager.api.common.branch;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;

import com.creativemd.ingameconfigmanager.api.common.segment.ConfigSegment;
import com.creativemd.ingameconfigmanager.api.core.IGCM;

public class ConfigSegmentCollection
{
	private ArrayList<ConfigSegment> segments;
	private Logger log = IGCM.logger;
	
	public ConfigSegmentCollection(ArrayList<ConfigSegment> segment)
	{
		this.segments = segment;
	}
	
	
	/** Getters */
	
	public ConfigSegment getSegmentByID(String segmentID)
	{
		for (int i = 0; i < segments.size(); i++) {
			if(segments.get(i).getID().equalsIgnoreCase(segmentID))
				return segments.get(i);
		}
		return null;
	}
	
	public Object getSegmentValue(String segmentID)
	{
		ConfigSegment segment = getSegmentByID(segmentID);
		if(segment != null)
			return segment.value;
		return null;
	}
	
	public ArrayList<ConfigSegment> asList()
	{
		return segments;
	}
}
