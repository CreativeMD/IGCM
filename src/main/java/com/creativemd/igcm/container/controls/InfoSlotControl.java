package com.creativemd.igcm.container.controls;

import com.creativemd.creativecore.common.gui.controls.container.SlotControlNoSync;
import com.creativemd.creativecore.common.slots.SlotPreview;
import com.creativemd.creativecore.common.utils.stack.InfoStack;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

public class InfoSlotControl extends SlotControlNoSync {
	
	public InfoStack info;
	public InventoryBasic inventory;
	
	public InfoSlotControl(IInventory inventory, int index, int x, int y, InfoStack info) {
		super(new SlotPreview(inventory, index, x, y));
		this.info = info;
		inventory = (InventoryBasic) slot.inventory;
		if (info != null)
			slot.putStack(info.getItemStack());
	}
	
	public void putInfo(InfoStack info) {
		this.info = info;
		if (info != null)
			slot.putStack(info.getItemStack());
		else
			slot.putStack(ItemStack.EMPTY);
	}
	
}