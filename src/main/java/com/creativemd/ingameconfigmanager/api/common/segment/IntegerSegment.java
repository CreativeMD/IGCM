package com.creativemd.ingameconfigmanager.api.common.segment;

import java.util.ArrayList;

import com.creativemd.creativecore.common.container.SubContainer;
import com.creativemd.creativecore.common.container.slot.ContainerControl;
import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.common.gui.controls.GuiButton;
import com.creativemd.creativecore.common.gui.controls.GuiControl;
import com.creativemd.creativecore.common.gui.controls.GuiStateButton;
import com.creativemd.creativecore.common.gui.controls.GuiTextfield;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IntegerSegment extends TitleSegment<Integer>{
	
	public int min;
	public int max;
	
	public IntegerSegment(String id, String title, Integer defaultValue) {
		this(id, title, defaultValue, 0, Integer.MAX_VALUE);
	}
	
	public IntegerSegment(String id, String title, Integer defaultValue, int min, int max) {
		super(id, title, defaultValue);
		this.min = min;
		this.max = max;
	}

	@Override
	public ArrayList<ContainerControl> createContainerControls(
			SubContainer gui, int x, int y, int maxWidth) {
		ArrayList<ContainerControl> controls = new ArrayList<ContainerControl>();
		
		return controls;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y,
			int maxWidth) {
		ArrayList<GuiControl> controls = super.createGuiControls(gui, x, y, maxWidth);
		controls.add(new GuiTextfield(getID(), "" + value, x+maxWidth-50, y, 40, 20).setNumbersOnly());
		return controls;
	}

	@Override
	public String createPacketInformation(boolean isServer) {
		if(!isServer && guiControls != null && guiControls.size() == 2)
		{
			int valueBefore = value;
			try{
				value = Integer.parseInt(((GuiTextfield)guiControls.get(1)).text);
			}catch(Exception e){
				value = valueBefore;
			}
			value = Math.max(min, value);
			value = Math.min(max, value);
		}
		return value.toString();
	}

	@Override
	public void receivePacketInformation(String input) {
		value = Integer.parseInt(input);
	}

	@Override
	public boolean contains(String search) {
		return super.contains(search) || value.toString().contains(search);
	}

}
