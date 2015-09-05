package com.creativemd.ingameconfigmanager.api.common.container.controls;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;

import com.creativemd.creativecore.common.container.slot.SlotControl;
import com.creativemd.creativecore.common.container.slot.SlotControlNoSync;
import com.creativemd.creativecore.common.container.slot.SlotPreview;
import com.creativemd.creativecore.common.gui.controls.GuiControl;
import com.creativemd.creativecore.common.gui.controls.container.GuiSlotControl;
import com.creativemd.creativecore.common.utils.stack.StackInfo;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class InfoSlotControl extends SlotControlNoSync{
	
	public StackInfo info;
	public InventoryBasic inventory;
	
	public InfoSlotControl(int x, int y, StackInfo info) {
		super(null);
		this.info = info;
		inventory = new InventoryBasic("slot", false, 1);
		slot = new SlotPreview(inventory, 0, x, y);
		if(info != null)
			slot.putStack(info.getItemStack());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiControl createGuiControl() {
		return new GuiSlotControl(slot.xDisplayPosition, slot.yDisplayPosition, this);
	}

}
