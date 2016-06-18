package com.creativemd.ingameconfigmanager.api.common.segment;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import com.creativemd.creativecore.gui.ContainerControl;
import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.container.SubContainer;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.gui.GuiComboBox;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		GuiComboBox box = new GuiComboBox(title, x+maxWidth-100, y, 90, new ArrayList<String>(Arrays.asList(options)));
		box.caption = value; 
		controls.add(box);
		return controls;
	}
	
	@Override
	public ArrayList<ContainerControl> createContainerControls(int x, int y, int maxWidth) {
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