package com.creativemd.ingameconfigmanager.api.common.segment;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.inventory.Slot;

import com.creativemd.creativecore.common.container.SubContainer;
import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.common.gui.controls.GuiControl;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ConfigSegmentText extends ConfigSegment<String>{

	public ConfigSegmentText(String id, String Title,String defaultValue) {
		super(id, Title, defaultValue);
		// TODO Auto-generated constructor stub
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleRendering(int maxWidth, Minecraft mc,
			FontRenderer fontRenderer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<Slot> getSlots(SubContainer gui, int x, int y, int maxWidth) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> getControls(SubGui gui, int x, int y,
			int maxWidth) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAllPacketInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void recieveAllPacketInformation(String input) {
		// TODO Auto-generated method stub
		
	}

}
