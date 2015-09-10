package com.creativemd.ingameconfigmanager.api.common.segment;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import com.creativemd.creativecore.common.container.SubContainer;
import com.creativemd.creativecore.common.container.slot.ContainerControl;
import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.common.gui.controls.GuiButton;
import com.creativemd.creativecore.common.gui.controls.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.GuiControl;
import com.creativemd.creativecore.common.gui.controls.GuiLabel;

import cpw.mods.fml.client.config.GuiSelectString;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SelectSegment extends TitleSegment<String>{
	
	public String[] options;
	
	public SelectSegment(String id, String title, String defaultValue, String... options) {
		super(id, title, defaultValue);
		this.options = options;
	}
	
	public SelectSegment(String id, String Title, int index, String... options) {
		this(id, Title, options[index], options);
		this.options = options;
	}
	
	public int getIndex()
	{
		return ArrayUtils.indexOf(options, value);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y,
			int maxWidth) {
		ArrayList<GuiControl> controls = super.createGuiControls(gui, x, y, maxWidth);
		GuiComboBox box = new GuiComboBox(title, x+maxWidth-90, y, 80, new ArrayList<String>(Arrays.asList(options)));
		box.caption = value; 
		controls.add(box);
		return controls;
	}
	
	@Override
	public ArrayList<ContainerControl> createContainerControls(
			SubContainer gui, int x, int y, int maxWidth) {
		return new ArrayList<ContainerControl>();
	}
	
	@Override
	public String createPacketInformation(boolean isServer) {
		if(!isServer && guiControls != null && guiControls.size() == 2)
			value = ((GuiComboBox)guiControls.get(1)).caption;
		return value;
	}
	
	@Override
	public void receivePacketInformation(String input) {
		value = input;
	}
	
}