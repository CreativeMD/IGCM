package com.creativemd.igcm.api.segments.advanced;

import java.util.ArrayList;

import com.creativemd.creativecore.gui.ContainerControl;
import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.container.SubContainer;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.container.SlotControl;
import com.creativemd.creativecore.gui.controls.container.SlotControlNoSync;
import com.creativemd.creativecore.slots.SlotPreview;
import com.creativemd.igcm.api.machine.RecipeMachine;
import com.creativemd.igcm.api.segments.ValueSegment;
import com.creativemd.igcm.utils.SearchUtils;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OutputSegment extends ValueSegment<ItemStack[]> {
	
	public RecipeMachine machine;
	
	public OutputSegment(String id, ItemStack[] defaultValue, RecipeMachine machine) {
		super(id, defaultValue);
		this.machine = machine;
	}
	
	public void empty() {
		for (int i = 0; i < value.length; i++) {
			SlotControlNoSync slot = (SlotControlNoSync) getContainerControl(getKey() + i);
			slot.slot.putStack(ItemStack.EMPTY);
		}
	}
	
	@Override
	public ArrayList<ContainerControl> createContainerControls(SubContainer container, int x, int y, int maxWidth) {
		ArrayList<ContainerControl> controls = super.createContainerControls(container, x, y, maxWidth);
		if (value == null)
			value = new ItemStack[machine.getOutputCount()];
		InventoryBasic basic = new InventoryBasic(getKey(), false, value.length);
		for (int i = 0; i < value.length; i++) {
			
			if (value[i] == null)
				value[i] = ItemStack.EMPTY;
			basic.setInventorySlotContents(i, value[i].copy());
			controls.add(new SlotControlNoSync(new SlotPreview(basic, i, x, y + i * 18)));
		}
		return controls;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y, int maxWidth) {
		return new ArrayList<GuiControl>();
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
		if (value == null)
			value = new ItemStack[machine.getOutputCount()];
		for (int i = 0; i < value.length; i++) {
			value[i] = new ItemStack(nbt.getCompoundTag(getKey() + i));
		}
	}
	
	@Override
	public void saveExtra(NBTTagCompound nbt) {
		for (int i = 0; i < value.length; i++) {
			nbt.setTag(getKey() + i, value[i].writeToNBT(new NBTTagCompound()));
		}
	}
	
	@Override
	public void saveFromControls() {
		super.saveFromControls();
		for (int i = 0; i < value.length; i++) {
			value[i] = ((SlotControl) getContainerControl(getKey() + i)).slot.getStack();
		}
	}
	
	@Override
	public void set(ItemStack[] newValue) {
		value = newValue;
	}
	
}
