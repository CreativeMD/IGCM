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

public class FloatSegment extends TitleSegment<Float>{
	
	public float min;
	public float max;
	
	public FloatSegment(String title, Float defaultValue) {
		this(title, defaultValue, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
	}
	
	public FloatSegment(String title, Float defaultValue, float min, float max) {
		super(title, defaultValue);
		this.min = min;
		this.max = max;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y,
			int maxWidth) {
		ArrayList<GuiControl> controls = super.createGuiControls(gui, x, y, maxWidth);
		controls.add(new GuiTextfield(getKey(), "" + value, x+maxWidth-50, y, 40, 14).setFloatOnly());
		return controls;
	}

	@Override
	public void loadExtra(NBTTagCompound nbt) {
		if(nbt.hasKey(getKey()))
			value = nbt.getFloat(getKey());
		else
			initDefault();
	}

	@Override
	public void saveExtra(NBTTagCompound nbt) {
		nbt.setFloat(getKey(), value);
	}

	@Override
	public void saveFromControls() {
		float valueBefore = value;
		try{
			set(Float.parseFloat(((GuiTextfield) getGuiControl(getKey())).text));
		}catch(Exception e){
			value = valueBefore;
		}
	}

	@Override
	public void set(Float newValue) {
		value = MathHelper.clamp(newValue, min, max);
	}
	
}
