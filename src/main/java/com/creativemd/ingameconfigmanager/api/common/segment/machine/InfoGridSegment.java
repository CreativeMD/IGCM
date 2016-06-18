package com.creativemd.ingameconfigmanager.api.common.segment.machine;

import java.util.ArrayList;

import com.creativemd.creativecore.common.utils.stack.StackInfo;
import com.creativemd.creativecore.common.utils.string.StringUtils;
import com.creativemd.creativecore.gui.ContainerControl;
import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.container.SubContainer;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.ingameconfigmanager.api.common.container.controls.InfoSlotControl;
import com.creativemd.ingameconfigmanager.api.common.segment.ConfigSegment;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InfoGridSegment extends ConfigSegment<StackInfo[]>{
	
	public RecipeSegment parent;

	public InfoGridSegment(String id, StackInfo[] defaultValue, RecipeSegment parent) {
		super(id, defaultValue);
		this.parent = parent;
	}
	
	public void empty()
	{
		for (int i = 0; i < containerControls.size(); i++) {
			((InfoSlotControl)containerControls.get(i)).slot.putStack(null);
			((InfoSlotControl)containerControls.get(i)).info = null;
		}
	}

	@Override
	public ArrayList<ContainerControl> createContainerControls(int x, int y, int maxWidth) {
		ArrayList<ContainerControl> slots = new ArrayList<ContainerControl>();
		if(value == null)
			value = new StackInfo[parent.machine.getWidth()*parent.machine.getHeight()];
		for (int i = 0; i < value.length; i++) {
			slots.add(new InfoSlotControl(5+x+i*18-i/parent.machine.getWidth()*parent.machine.getWidth()*18, y+i/parent.machine.getWidth()*18, value[i]));
		}
		return slots;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y,
			int maxWidth) {
		return new ArrayList<GuiControl>();
	}

	@Override
	public String createPacketInformation(boolean isServer) {
		if(!isServer && containerControls.size() == value.length)
		{
			for (int i = 0; i < value.length; i++) {
				value[i] = ((InfoSlotControl) containerControls.get(i)).info;
			}
		}
		return null;
	}

	@Override
	public void receivePacketInformation(String input) {
		value = new StackInfo[parent.machine.getWidth()*parent.machine.getHeight()];
		Object[] objects = StringUtils.StringToObjects(input);
		for (int i = 0; i < objects.length; i++) {
			value[i] = (StackInfo) objects[i];
		}
	}

	@Override
	public boolean contains(String search) {
		return false;
	}

}
