package com.creativemd.igcm.api.segments.advanced;

import java.util.ArrayList;

import com.creativemd.creativecore.common.gui.ContainerControl;
import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.gui.controls.container.SlotControlNoSync;
import com.creativemd.creativecore.common.slots.SlotPreview;
import com.creativemd.igcm.api.machine.RecipeMachine;
import com.creativemd.igcm.api.segments.ValueSegment;
import com.creativemd.igcm.utils.SearchUtils;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GridSegment extends ValueSegment<ItemStack[]> {
	
	public RecipeMachine machine;
	
	public GridSegment(String id, ItemStack[] defaultValue, RecipeMachine machine) {
		super(id, defaultValue);
		this.machine = machine;
	}
	
	@Override
	public ArrayList<ContainerControl> createContainerControls(SubContainer container, int x, int y, int maxWidth) {
		ArrayList<ContainerControl> controls = super.createContainerControls(container, x, y, maxWidth);
		if (value == null)
			value = new ItemStack[machine.getWidth() * machine.getHeight()];
		for (int i = 0; i < value.length; i++) {
			InventoryBasic basic = new InventoryBasic("basic", false, 1);
			if (value[i] == null)
				value[i] = ItemStack.EMPTY;
			basic.setInventorySlotContents(0, value[i].copy());
			controls.add(new SlotControlNoSync(new SlotPreview(basic, 0, x + i * 18 - i / machine.getWidth() * machine.getWidth() * 18, y + i / machine.getWidth() * 18)));
		}
		return controls;
	}
	
	@Override
	public boolean contains(String search) {
		if (!super.contains(search)) {
			for (int i = 0; i < value.length; i++) {
				if (SearchUtils.canStackBeFound(value[i], search))
					return true;
			}
			return false;
		}
		return true;
	}
	
	@Override
	public void loadExtra(NBTTagCompound nbt) {
		
	}
	
	@Override
	public void saveExtra(NBTTagCompound nbt) {
		
	}
	
	@Override
	public void saveFromControls() {
		
	}
	
	@Override
	public void set(ItemStack[] newValue) {
		value = newValue;
	}
	
}
