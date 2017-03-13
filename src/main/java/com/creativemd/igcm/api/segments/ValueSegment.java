package com.creativemd.igcm.api.segments;

import java.util.ArrayList;

import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.gui.GuiLabel;
import com.creativemd.igcm.api.ConfigSegment;
import com.creativemd.igcm.utils.SearchUtils;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ValueSegment<T> extends ConfigSegment {
	
	public T defaultValue;
	
	public T value;
	
	public ValueSegment(String title, T defaultValue) {
		super(title);
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}
	
	@Override
	public void initDefault()
	{
		value = defaultValue;
		super.initDefault();
	}
	
	@Override
	public boolean contains(String search)
	{
		return title.toLowerCase().contains(search) || getKey().toLowerCase().contains(search) || value.toString().toLowerCase().contains(search);
	}
	
}
