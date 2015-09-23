package com.creativemd.ingameconfigmanager.api.common.segment;

import java.util.ArrayList;

import com.creativemd.creativecore.common.container.SubContainer;
import com.creativemd.creativecore.common.container.slot.ContainerControl;
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
	
	public ArrayList<ContainerControl> containerControls;
	
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> guiControls;
	
	public int xOffset;
	public int yOffset;
	
	public ConfigSegment(String id, T defaultValue) //, SegmentType segmentType)
	{
		this.ID = id;
		//this.segmentType = segmentType;
		this.value = defaultValue;
		this.subSegments = new ArrayList<ConfigSegment>();
		parent = null;
	}
	
	public ConfigSegment<T> setOffset(int x, int y)
	{
		this.xOffset = x;
		this.yOffset = y;
		return this;
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
	
	public void onSegmentLoaded(int x, int y, int maxWidth) {}
	
	/*public void raiseChangedEvent()
	{
	
	}*/
	
	@SideOnly(Side.CLIENT)
	public int getHeight()
	{
		int maxHeight = 0;
		for (int i = 0; i < subSegments.size(); i++) {
			int tempHeight = subSegments.get(i).yOffset + subSegments.get(i).getHeight();
			if(tempHeight > maxHeight)
				maxHeight = tempHeight;
		}
		
		for (int i = 0; i < guiControls.size(); i++) {
			int tempHeight = guiControls.get(i).posY + guiControls.get(i).height;
			if(tempHeight > maxHeight)
				maxHeight = tempHeight;
		}
		return maxHeight;
	}
	
	public abstract ArrayList<ContainerControl> createContainerControls(SubContainer gui, int x, int y, int maxWidth);
	
	@SideOnly(Side.CLIENT)
	public abstract ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y, int maxWidth);
	
	public abstract String createPacketInformation(boolean isServer);
	
	public abstract void receivePacketInformation(String input);
	
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
	
	public abstract boolean contains(String search);
	
	/*public SegmentType getSegmentType()
	{
		return this.segmentType;
	}*/
}
