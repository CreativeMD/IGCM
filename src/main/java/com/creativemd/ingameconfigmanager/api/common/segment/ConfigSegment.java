package com.creativemd.ingameconfigmanager.api.common.segment;

import com.creativemd.ingameconfigmanager.api.common.segment.inputType.InputType;

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

public class ConfigSegment
{
	public enum SegmentType
	{
		OptionButton,
		//OptionSelectField, - optional
		GuiSlot,
		GuiTextField,
		GuiNummericField,
		Collection,
		LineBreaker;
	}

	public int Width;
	public int Height;
	public int PosX;
	public int PosY;
	private SegmentType segmentType;
	private InputType inputType;
	private Object Value;
	private Object defaultValue;
	private String ID;
	
	public ConfigSegment(String id, SegmentType segmentType, InputType inputType, Object defaultValue, int Width, int Height, int posX, int posY)
	{
		this.ID = id;
		this.segmentType = segmentType;
		this.inputType = inputType;
		this.defaultValue = defaultValue;
		this.Width = Width;
		this.Height = Height;
		this.PosX = posX;
		this.PosY = posY;
	}
	
	/** Setters */
	
	public ConfigSegment setDefaultValue(Object defaultValue)
	{
		this.defaultValue = defaultValue;
		return this;
	}
	
	public ConfigSegment setValue(Object value)
	{
		this.Value = value;
		return this;
	}
	
	public ConfigSegment setDimensions(int width, int height, int posX, int posY)
	{
		this.Width = width;
		this.Height = height;
		this.PosX = posX;
		this.PosY = posY;
		return this;
	}
	
	/** Getters */
	
	public String getID()
	{
		return this.ID;
	}
	
	public SegmentType getSegmentType()
	{
		return this.segmentType;
	}
	
	public Object getDefaultValue()
	{
		return this.defaultValue;
	}
	
	public Object getValue()
	{
		return this.Value;
	}
}
