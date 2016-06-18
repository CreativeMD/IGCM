package com.creativemd.ingameconfigmanager.api.common.segment;

import java.util.ArrayList;

import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.gui.GuiAnalogeSlider;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		controls.add(new GuiAnalogeSlider(getID(), x+maxWidth-80, y, 70, 14, value, min, max));
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
