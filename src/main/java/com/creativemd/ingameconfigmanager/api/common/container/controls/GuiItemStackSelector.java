package com.creativemd.ingameconfigmanager.api.common.container.controls;

import java.util.ArrayList;
import java.util.Iterator;

import com.creativemd.creativecore.client.avatar.AvatarItemStack;
import com.creativemd.creativecore.common.container.slot.SlotControl;
import com.creativemd.creativecore.common.container.slot.SlotControlNoSync;
import com.creativemd.creativecore.common.container.slot.SlotPreview;
import com.creativemd.creativecore.common.gui.controls.GuiAvatarLabel;
import com.creativemd.creativecore.common.gui.controls.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.GuiComboBoxExtension;
import com.creativemd.creativecore.common.gui.controls.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.GuiLabelClickable;
import com.creativemd.creativecore.common.gui.controls.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.container.GuiSlotControl;
import com.creativemd.creativecore.common.gui.event.ControlClickEvent;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.client.gui.GuiSlot;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class GuiItemStackSelector extends GuiComboBoxExtension{
	
	//public GuiInvSelectorExtension extension;
	public ArrayList<ItemStack> stacks;
	public ArrayList<ItemStack> inv;
	
	public GuiItemStackSelector(String name, EntityPlayer player, int x, int y, int width, int height, GuiComboBox comboBox) {
		super(name, player, comboBox, x, y, width, height, new ArrayList<String>());
		//this.extension = extension;
		
		stacks = new ArrayList<ItemStack>();
		inv = new ArrayList<ItemStack>();
		
		for (int i = 0; i < player.inventory.mainInventory.length; i++) {
			if(player.inventory.mainInventory[i] != null)
				inv.add(player.inventory.mainInventory[i]);
		}
		//CreativeTabs.tabAllSearch.displayAllReleventItems(stacks);
		Iterator iterator = Item.itemRegistry.iterator();

        while (iterator.hasNext())
        {
            Item item = (Item)iterator.next();

            if (item != null && item.getCreativeTab() != null)
            {
                item.getSubItems(item, (CreativeTabs)null, stacks);
            }
        }
		refreshControls();
	}
	
	@Override
	public void refreshControls()
	{
		if(stacks != null)
		{
			gui.controls.clear();
			/*for (int i = 0; i < lines.size(); i++) {
				int color = 14737632;
				if(i == selected)
					color = 16777000;
				GuiLabelClickable label = new GuiAvatarLabel(lines.get(i), 3, 1+i*22, color, new AvatarItemStack(stacks.get(i)));
				label.width = width-20;
				label.height = 22;
				addControl(label);
			}*/
			int height = 0;
			GuiLabel label = new GuiLabel("Inventory", 3, height);
			label.width = width-20;
			label.height = 14;
			addControl(label);
			height += label.height;
			
			int SlotsPerRow = width/18;
			for (int i = 0; i < inv.size(); i++) {
				InventoryBasic basic = new InventoryBasic("", false, 1);
				basic.setInventorySlotContents(0, inv.get(i));
				
				int row = i/SlotsPerRow;
				addControl(new SlotControlNoSync(new SlotPreview(basic, 0, (i-row*SlotsPerRow)*18, height+row*18)));
			}
			height += Math.floor(inv.size()/SlotsPerRow+1)*18;
			
			label = new GuiLabel("Items", 3, height);
			label.width = width-20;
			label.height = 14;
			addControl(label);
			height += label.height;

			for (int i = 0; i < stacks.size(); i++) {
				InventoryBasic basic = new InventoryBasic("", false, 1);
				basic.setInventorySlotContents(0, stacks.get(i));
				int row = i/SlotsPerRow;
				addControl(new SlotControlNoSync(new SlotPreview(basic, 0, (i-row*SlotsPerRow)*18, height+row*18)));
			}
		}
	}
	
	@Override
	@CustomEventSubscribe
	public void onLabelClicked(ControlClickEvent event)
	{
		if(event.source instanceof GuiSlotControl)
		{
			((GuiInvSelector)comboBox).addAndSelectStack(((GuiSlotControl) event.source).slot.slot.getStack().copy());
			comboBox.closeBox();
		}
	}
	
	@Override
	public boolean mouseScrolled(int posX, int posY, int scrolled){
		this.scrolled -= scrolled*30;
		onScrolled();
		return true;
	}
}
