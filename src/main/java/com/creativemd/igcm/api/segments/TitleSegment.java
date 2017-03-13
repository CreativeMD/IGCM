package com.creativemd.igcm.api.segments;

import java.util.ArrayList;

import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.gui.GuiLabel;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TitleSegment<T> extends ValueSegment<T>{
	
	public String[] tooltip = null;
	
	public TitleSegment(String title, T defaultValue) {
		super(title, defaultValue);
	}
	
	public ValueSegment<T> setToolTip(String... tooltip)
	{
		this.tooltip = tooltip;
		return this;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y,
			int maxWidth) {
		ArrayList<GuiControl> controls = super.createGuiControls(gui, x, y, maxWidth);
		GuiLabel label = new GuiLabel(title, x+10, y+5);
		if(tooltip != null)
			label.setCustomTooltip(tooltip);
		controls.add(label);
		return controls;
	}
}
