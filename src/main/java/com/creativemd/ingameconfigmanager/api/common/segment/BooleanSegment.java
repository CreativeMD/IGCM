package com.creativemd.ingameconfigmanager.api.common.segment;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import com.creativemd.creativecore.common.container.SubContainer;
import com.creativemd.creativecore.common.container.slot.ContainerControl;
import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.common.gui.controls.GuiButton;
import com.creativemd.creativecore.common.gui.controls.GuiControl;
import com.creativemd.creativecore.common.gui.controls.GuiStateButton;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BooleanSegment extends TitleSegment<Boolean>{

	public BooleanSegment(String id, String Title, Boolean defaultValue) {
		super(id, Title, defaultValue);
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
		controls.add(new GuiStateButton(getID(), value.toString().replace("f", "F").replace("t", "T"), x+maxWidth-50, y, 40, 20, "True", "False"));
		return controls;
	}

	@Override
	public String createPacketInformation() {
		if(guiControls != null && guiControls.size() == 2)
			value = Boolean.parseBoolean(((GuiButton)guiControls.get(1)).caption);
		return value.toString();
	}

	@Override
	public void receivePacketInformation(String input) {
		value = Boolean.parseBoolean(input);
	}

	@Override
	public boolean contains(String search) {
		return super.contains(search) || value.toString().contains(search);
	}

}
