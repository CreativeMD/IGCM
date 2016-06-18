package com.creativemd.ingameconfigmanager.api.common.segment.machine;

import java.util.ArrayList;

import com.creativemd.creativecore.gui.ContainerControl;
import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.container.SubContainer;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.container.SlotControlNoSync;
import com.creativemd.creativecore.slots.SlotPreview;
import com.creativemd.ingameconfigmanager.api.common.machine.RecipeMachine;
import com.creativemd.ingameconfigmanager.api.common.segment.ConfigSegment;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GridSegment extends ConfigSegment<ItemStack[]>{
	
	public RecipeMachine machine;
	
	public GridSegment(String id, ItemStack[] defaultValue, RecipeMachine machine) {
		super(id, defaultValue);
		this.machine = machine;
	}

	@Override
	public ArrayList<ContainerControl> createContainerControls(int x, int y, int maxWidth) {
		ArrayList<ContainerControl> controls = new ArrayList<ContainerControl>();
		if(value == null)
			value = new ItemStack[machine.getWidth()*machine.getHeight()];
		for (int i = 0; i < value.length; i++) {
			InventoryBasic basic = new InventoryBasic("basic", false, 1);
			if(value[i] != null)
				basic.setInventorySlotContents(0, value[i].copy());
			controls.add(new SlotControlNoSync(new SlotPreview(basic, 0, x+i*18-i/machine.getWidth()*machine.getWidth()*18, y+i/machine.getWidth()*18)));
		}
		return controls;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y,
			int maxWidth) {
		return new ArrayList<GuiControl>();
	}

	@Override
	public String createPacketInformation(boolean isServer) {
		return "null";
	}

	@Override
	public void receivePacketInformation(String input) {
		
	}
	
	@Override
	public boolean contains(String search) {
		return false;
	}

}
