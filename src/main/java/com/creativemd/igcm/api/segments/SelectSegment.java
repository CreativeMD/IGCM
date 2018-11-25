package com.creativemd.igcm.api.segments;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SelectSegment extends TitleSegment<String> {
	
	public String[] options;
	
	public SelectSegment(String title, String defaultValue, String... options) {
		super(title, defaultValue);
		this.options = options;
	}
	
	public SelectSegment(String title, int index, String... options) {
		this(title, options[index], options);
		this.options = options;
	}
	
	public int getIndex() {
		return ArrayUtils.indexOf(options, value);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y, int maxWidth) {
		ArrayList<GuiControl> controls = super.createGuiControls(gui, x, y, maxWidth);
		GuiComboBox box = new GuiComboBox(getKey(), x + maxWidth - 100, y, 90, new ArrayList<String>(Arrays.asList(options)));
		box.caption = value;
		controls.add(box);
		return controls;
	}
	
	@Override
	public void loadExtra(NBTTagCompound nbt) {
		if (nbt.hasKey(getKey()))
			value = nbt.getString(getKey());
		else
			initDefault();
	}
	
	@Override
	public void saveExtra(NBTTagCompound nbt) {
		nbt.setString(getKey(), value);
	}
	
	@Override
	public void saveFromControls() {
		set(((GuiComboBox) getGuiControl(getKey())).caption);
	}
	
	@Override
	public void set(String newValue) {
		if (ArrayUtils.contains(options, newValue))
			value = newValue;
	}
	
	@Override
	public String[] getPossibleValues() {
		return options;
	}
	
}