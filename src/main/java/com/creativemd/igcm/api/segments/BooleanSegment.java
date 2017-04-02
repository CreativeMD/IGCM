package com.creativemd.igcm.api.segments;

import java.util.ArrayList;

import com.creativemd.creativecore.gui.ContainerControl;
import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.container.SubContainer;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.gui.controls.gui.GuiStateButton;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BooleanSegment extends TitleSegment<Boolean>{

	public BooleanSegment(String title, Boolean defaultValue) {
		super(title, defaultValue);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y,
			int maxWidth) {
		ArrayList<GuiControl> controls = super.createGuiControls(gui, x, y, maxWidth);
		controls.add(new GuiStateButton(getKey(), value.toString().replace("f", "F").replace("t", "T"), x+maxWidth-50, y, 40, 14, "True", "False"));
		return controls;
	}

	@Override
	public void loadExtra(NBTTagCompound nbt) {
		if(nbt.hasKey(getKey()))
			value = nbt.getBoolean(getKey());
		else
			initDefault();
	}

	@Override
	public void saveExtra(NBTTagCompound nbt) {
		nbt.setBoolean(getKey(), value);
	}

	@Override
	public void saveFromControls() {
		set(Boolean.parseBoolean(((GuiButton) getGuiControl(getKey())).caption));
	}

	@Override
	public void set(Boolean newValue) {
		value = newValue;
	}
	
	@Override
	public String[] getPossibleValues() {
		return new String[]{"true", "false"};
	}

}
