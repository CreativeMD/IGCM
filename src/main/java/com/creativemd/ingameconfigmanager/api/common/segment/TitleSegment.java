package com.creativemd.ingameconfigmanager.api.common.segment;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import com.creativemd.creativecore.common.container.SubContainer;
import com.creativemd.creativecore.common.container.slot.ContainerControl;
import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.common.gui.controls.GuiControl;
import com.creativemd.creativecore.common.gui.controls.GuiLabel;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class TitleSegment<T> extends ConfigSegment<T>{
	public String title;
	
	public TitleSegment(String id, String title, T defaultValue) {
		super(id, defaultValue);
		this.title = title;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y,
			int maxWidth) {
		ArrayList<GuiControl> controls = new ArrayList<GuiControl>();
		controls.add(new GuiLabel(title, x+10, y+5));
		return controls;
	}
	
	@Override
	public boolean contains(String search) {
		return title.contains(search);
	}
}
