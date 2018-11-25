package com.creativemd.igcm.api.segments.advanced;

import java.util.ArrayList;

import com.creativemd.creativecore.common.gui.ContainerControl;
import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.utils.stack.InfoStack;
import com.creativemd.igcm.api.machine.RecipeMachine;
import com.creativemd.igcm.api.segments.ValueSegment;
import com.creativemd.igcm.container.controls.InfoSlotControl;
import com.creativemd.igcm.utils.SearchUtils;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InfoGridSegment extends ValueSegment<InfoStack[]> {
	
	public RecipeMachine machine;
	
	public InfoGridSegment(String id, InfoStack[] defaultValue, RecipeMachine machine) {
		super(id, defaultValue);
		this.machine = machine;
	}
	
	public void empty() {
		for (int i = 0; i < value.length; i++) {
			InfoSlotControl slot = (InfoSlotControl) getContainerControl(getKey() + i);
			slot.slot.putStack(ItemStack.EMPTY);
			slot.info = null;
		}
	}
	
	@Override
	public ArrayList<ContainerControl> createContainerControls(SubContainer container, int x, int y, int maxWidth) {
		ArrayList<ContainerControl> slots = super.createContainerControls(container, x, y, maxWidth);
		if (value == null)
			value = new InfoStack[machine.getWidth() * machine.getHeight()];
		InventoryBasic basic = new InventoryBasic(getKey(), false, value.length);
		for (int i = 0; i < value.length; i++) {
			slots.add(new InfoSlotControl(basic, i, 5 + x + i * 18 - i / machine.getWidth() * machine.getWidth() * 18, y + i / machine.getWidth() * 18, value[i]));
		}
		return slots;
	}
	
	@Override
	public void loadExtra(NBTTagCompound nbt) {
		if (value == null)
			value = new InfoStack[machine.getWidth() * machine.getHeight()];
		for (int i = 0; i < value.length; i++) {
			if (nbt.hasKey(getKey() + i))
				value[i] = InfoStack.parseNBT(nbt.getCompoundTag(getKey() + i));
		}
	}
	
	@Override
	public void saveExtra(NBTTagCompound nbt) {
		for (int i = 0; i < value.length; i++) {
			if (value[i] != null)
				nbt.setTag(getKey() + i, value[i].writeToNBT(new NBTTagCompound()));
		}
	}
	
	@Override
	public boolean contains(String search) {
		if (!super.contains(search)) {
			for (int i = 0; i < value.length; i++) {
				if (SearchUtils.canInfoBeFound(value[i], search))
					return true;
			}
			return false;
		}
		return true;
	}
	
	@Override
	public void saveFromControls() {
		super.saveFromControls();
		for (int i = 0; i < value.length; i++) {
			value[i] = ((InfoSlotControl) getContainerControl(getKey() + i)).info;
		}
	}
	
	@Override
	public void set(InfoStack[] newValue) {
		value = newValue;
	}
	
}
