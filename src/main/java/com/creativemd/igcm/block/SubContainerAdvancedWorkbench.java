package com.creativemd.igcm.block;

import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.slots.SlotOutput;
import com.creativemd.creativecore.common.utils.mc.InventoryUtils;
import com.creativemd.creativecore.common.utils.mc.WorldUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SubContainerAdvancedWorkbench extends SubContainer {
	
	public InventoryBasic crafting = new InventoryBasic("crafting", false, (int) Math.pow(BlockAdvancedWorkbench.gridSize, 2));
	public InventoryBasic output = new InventoryBasic("output", false, BlockAdvancedWorkbench.outputs);
	
	public SubContainerAdvancedWorkbench(EntityPlayer player) {
		super(player);
	}
	
	@Override
	public void createControls() {
		for (int y = 0; y < BlockAdvancedWorkbench.gridSize; y++) {
			for (int x = 0; x < BlockAdvancedWorkbench.gridSize; x++) {
				addSlotToContainer(new Slot(crafting, y * BlockAdvancedWorkbench.gridSize + x, 8 + x * 18, 5 + y * 18));
			}
		}
		for (int i = 0; i < BlockAdvancedWorkbench.outputs; i++) {
			addSlotToContainer(new SlotOutput(output, i, 132 + (i - i / 2 * 2) * 18, 41 + i / 2 * 18));
		}
		
		//System.out.println("Creating!");
		addPlayerSlotsToContainer(player, 8, 120);
	}
	
	@Override
	public void onPacketReceive(NBTTagCompound nbt) {
		if (nbt.getInteger("type") == 0) {
			AdvancedGridRecipe recipe = null;
			for (int i = 0; i < BlockAdvancedWorkbench.recipes.size(); i++) {
				if (BlockAdvancedWorkbench.recipes.get(i).isValidRecipe(crafting, 6, 6)) {
					recipe = BlockAdvancedWorkbench.recipes.get(i);
					break;
				}
			}
			
			if (recipe != null) {
				
				for (int i = 0; i < recipe.output.length; i++) {
					if (recipe.output[i] != null && !recipe.output[i].isEmpty()) {
						ItemStack stack = recipe.output[i].copy();
						if (!InventoryUtils.addItemStackToInventory(output, stack))
							if (!InventoryUtils.addItemStackToInventory(player.inventory, stack))
								WorldUtils.dropItem(player, stack);
					}
				}
				recipe.consumeRecipe(crafting, BlockAdvancedWorkbench.gridSize, BlockAdvancedWorkbench.gridSize);
			}
		}
	}
	
	@Override
	public void onClosed() {
		for (int i = 0; i < crafting.getSizeInventory(); i++) {
			if (crafting.getStackInSlot(i) != null)
				WorldUtils.dropItem(player, crafting.getStackInSlot(i));
		}
		
		for (int i = 0; i < output.getSizeInventory(); i++) {
			if (output.getStackInSlot(i) != null)
				WorldUtils.dropItem(player, output.getStackInSlot(i));
		}
	}
	
}
