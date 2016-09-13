package com.creativemd.ingameconfigmanager.api.common.segment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.gui.GuiLabel;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TitleSegment<T> extends ConfigSegment<T>{
	public String title;
	
	public String[] tooltip = null;
	
	public TitleSegment(String id, String title, T defaultValue) {
		super(id, defaultValue);
		this.title = title;
	}
	
	public TitleSegment<T> setToolTip(String... tooltip)
	{
		this.tooltip = tooltip;
		return this;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y,
			int maxWidth) {
		ArrayList<GuiControl> controls = new ArrayList<GuiControl>();
		GuiLabel label = new GuiLabel(title, x+10, y+5);
		if(tooltip != null)
			label.setCustomTooltip(tooltip);
		controls.add(label);
		return controls;
	}
	
	@Override
	public boolean contains(String search) {
		return title.contains(search);
	}
}
