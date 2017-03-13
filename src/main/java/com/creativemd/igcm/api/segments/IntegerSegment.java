package com.creativemd.igcm.api.segments;

import java.util.ArrayList;

import com.creativemd.creativecore.gui.ContainerControl;
import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.container.SubContainer;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.gui.GuiTextfield;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class IntegerSegment extends TitleSegment<Integer>{
	
	public int min;
	public int max;
	
	public IntegerSegment(String title, Integer defaultValue) {
		this(title, defaultValue, 0, Integer.MAX_VALUE);
	}
	
	public IntegerSegment(String title, Integer defaultValue, int min, int max) {
		super(title, defaultValue);
		this.min = min;
		this.max = max;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y,
			int maxWidth) {
		ArrayList<GuiControl> controls = super.createGuiControls(gui, x, y, maxWidth);
		controls.add(new GuiTextfield(getKey(), "" + value, x+maxWidth-50, y, 40, 14).setNumbersOnly());
		return controls;
	}

	@Override
	public void loadExtra(NBTTagCompound nbt) {
		if(nbt.hasKey(getKey()))
			value = MathHelper.clamp(nbt.getInteger(getKey()), min, max);
		else
			initDefault();
	}

	@Override
	public void saveExtra(NBTTagCompound nbt) {
		nbt.setInteger(getKey(), value);
	}

	@Override
	public void saveFromControls() {
		int valueBefore = value;
		try{
			value = MathHelper.clamp(Integer.parseInt(((GuiTextfield) getGuiControl(getKey())).text), min, max);;
		}catch(Exception e){
			value = valueBefore;
		}
	}

}
