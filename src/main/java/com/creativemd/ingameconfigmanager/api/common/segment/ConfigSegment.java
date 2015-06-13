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
package com.creativemd.ingameconfigmanager.api.common.segment;

import java.util.ArrayList;

import com.creativemd.creativecore.common.container.SubContainer;
import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.common.gui.controls.GuiControl;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.inventory.Slot;

public abstract class ConfigSegment<T>
{
	 
	/*public static enum SegmentType
	{
		Select,
		Boolean,
		String,
		Int,
		Float,
		Custom;
	}*/
	
	protected ArrayList<ConfigSegment> subSegments;
	public ConfigSegment parent;
	//private SegmentType segmentType;
	public T value;
	private String ID;
	public String Title;
	
	public ConfigSegment(String id, String Title, T defaultValue) //, SegmentType segmentType)
	{
		this.ID = id;
		this.Title = Title;
		//this.segmentType = segmentType;
		this.value = defaultValue;
		this.subSegments = new ArrayList<ConfigSegment>();
		parent = null;
	}
	
	public ArrayList<ConfigSegment> getSubSegments()
	{
		return subSegments;
	}
	
	public ConfigSegment addSubSegment(ConfigSegment subSegment)
	{
		subSegments.add(subSegment);
		subSegment.parent = this;
		return this;
	}
	
	public void onSubSegmentChanged(ConfigSegment segment){}
	
	public void raiseChangedEvent()
	{
		//TODO Add something here
	}
	
	@SideOnly(Side.CLIENT)
	public abstract int getHeight();
	
	@SideOnly(Side.CLIENT)
	public abstract void handleRendering(int maxWidth, Minecraft mc, FontRenderer fontRenderer);
	
	@SideOnly(Side.CLIENT)
	public abstract ArrayList<Slot> getSlots(SubContainer gui, int x, int y, int maxWidth);
	
	@SideOnly(Side.CLIENT)
	public abstract ArrayList<GuiControl> getControls(SubGui gui, int x, int y, int maxWidth);
	
	public abstract String getAllPacketInformation();
	
	public abstract void recieveAllPacketInformation(String input);
	
	public ArrayList<ConfigSegment> getAllSegments()
	{
		ArrayList<ConfigSegment> segments = new ArrayList<ConfigSegment>();
		segments.add(this);
		for (int i = 0; i < segments.size(); i++) {
			segments.addAll(segments.get(i).getAllSegments());
		}
		return segments;
	}
	
	public String getID()
	{
		return this.ID;
	}
	
	/*public SegmentType getSegmentType()
	{
		return this.segmentType;
	}*/
}
