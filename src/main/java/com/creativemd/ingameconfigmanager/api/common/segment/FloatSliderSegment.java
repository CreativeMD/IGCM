package com.creativemd.ingameconfigmanager.api.common.segment;

import java.util.ArrayList;

import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.common.gui.controls.GuiAnalogeSlider;
import com.creativemd.creativecore.common.gui.controls.GuiControl;
import com.creativemd.creativecore.common.gui.controls.GuiTextfield;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FloatSliderSegment extends FloatSegment{
	
	public FloatSliderSegment(String id, String title, Float defaultValue, float min, float max) {
		super(id, title, defaultValue, min, max);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y,
			int maxWidth) {
		ArrayList<GuiControl> controls = super.createGuiControls(gui, x, y, maxWidth);
		controls.remove(1);
		controls.add(new GuiAnalogeSlider(getID(), x+maxWidth-80, y, 70, 20, 0, value, min, max));
		return controls;
	}
	
	@Override
	public String createPacketInformation(boolean isServer) {
		if(!isServer && guiControls != null && guiControls.size() == 2)
		{
			float valueBefore = value;
			try{
				value = ((GuiAnalogeSlider)guiControls.get(1)).value;
			}catch(Exception e){
				value = valueBefore;
			}
			value = Math.max(min, value);
			value = Math.min(max, value);
		}
		return value.toString();
	}
	

}
