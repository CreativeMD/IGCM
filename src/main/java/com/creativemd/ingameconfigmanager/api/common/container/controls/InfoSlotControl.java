package com.creativemd.ingameconfigmanager.api.common.container.controls;

import com.creativemd.creativecore.common.utils.stack.StackInfo;
import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.controls.container.SlotControlNoSync;
import com.creativemd.creativecore.gui.controls.container.client.GuiSlotControl;
import com.creativemd.creativecore.slots.SlotPreview;

import net.minecraft.inventory.InventoryBasic;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InfoSlotControl extends SlotControlNoSync{
	
	public StackInfo info;
	public InventoryBasic inventory;
	
	public InfoSlotControl(int x, int y, StackInfo info) {
		super(new SlotPreview(new InventoryBasic("slot"+ x + "-" + y, false, 1), 0, x, y));
		this.info = info;
		inventory = (InventoryBasic) slot.inventory;
		if(info != null)
			slot.putStack(info.getItemStack());
	}

}
