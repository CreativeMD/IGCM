package com.creativemd.igcm.api.segments;

import java.util.ArrayList;

import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.gui.GuiSteppedSlider;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class IntegerSliderSegment extends IntegerSegment {
	
	public IntegerSliderSegment(String title, Integer defaultValue, int min, int max) {
		super(title, defaultValue, min, max);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y, int maxWidth) {
		ArrayList<GuiControl> controls = super.createGuiControls(gui, x, y, maxWidth);
		controls.remove(1);
		controls.add(new GuiSteppedSlider(getKey(), x + maxWidth - 80, y, 70, 14, value, min, max));
		return controls;
	}
	
	@Override
	public void saveFromControls() {
		int valueBefore = value;
		try {
			set((int) ((GuiSteppedSlider) getGuiControl(getKey())).value);
		} catch (Exception e) {
			value = valueBefore;
		}
	}
	
}
