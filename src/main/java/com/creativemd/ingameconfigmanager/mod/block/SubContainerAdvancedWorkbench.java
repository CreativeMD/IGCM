package com.creativemd.ingameconfigmanager.mod.block;

import com.creativemd.creativecore.common.container.SubContainer;
import com.creativemd.creativecore.common.container.slot.SlotOutput;
import com.creativemd.creativecore.common.gui.event.container.SlotChangeEvent;
import com.creativemd.creativecore.common.recipe.GridRecipe;
import com.creativemd.creativecore.common.utils.InventoryUtils;
import com.creativemd.creativecore.common.utils.WorldUtils;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SubContainerAdvancedWorkbench extends SubContainer{
	
	public InventoryBasic crafting = new InventoryBasic("crafting", false, (int) Math.pow(BlockAdvancedWorkbench.gridSize, 2));
	public InventoryBasic output = new InventoryBasic("output", false, BlockAdvancedWorkbench.outputs);
	
	public SubContainerAdvancedWorkbench(EntityPlayer player) {
		super(player);
	}

	@Override
	public void createControls() {
		for (int y = 0; y < BlockAdvancedWorkbench.gridSize; y++) {
			for (int x = 0; x < BlockAdvancedWorkbench.gridSize; x++) {
				addSlotToContainer(new Slot(crafting, y*BlockAdvancedWorkbench.gridSize+x, 8+x*18, 5+y*18));
			}
		}
		for (int i = 0; i < BlockAdvancedWorkbench.outputs; i++) {
			addSlotToContainer(new SlotOutput(output, i, 132+(i-i/2*2)*18, 41+i/2*18));
		}
		
		
		addPlayerSlotsToContainer(player, 8, 120);
	}

	@Override
	public void onGuiPacket(int controlID, NBTTagCompound nbt, EntityPlayer player) {
		if(controlID == 0)
		{
			AdvancedGridRecipe recipe = null;
			for (int i = 0; i < BlockAdvancedWorkbench.recipes.size(); i++) {
				if(BlockAdvancedWorkbench.recipes.get(i).isValidRecipe(crafting, 6, 6))
				{
					recipe = BlockAdvancedWorkbench.recipes.get(i);
					break;
				}
			}
			
			if(recipe != null)
			{
				
				for (int i = 0; i < recipe.output.length; i++) {
					if(recipe.output[i] != null)
					{
						ItemStack stack = recipe.output[i].copy();
						if(!InventoryUtils.addItemStackToInventory(output, stack))
							if(!InventoryUtils.addItemStackToInventory(player.inventory, stack))
								WorldUtils.dropItem(player.worldObj, stack, (int)player.posX, (int)player.posY, (int)player.posZ);
					}
				}
				recipe.consumeRecipe(crafting, BlockAdvancedWorkbench.gridSize, BlockAdvancedWorkbench.gridSize);
			}
		}
	}
	
	@Override
	public void onGuiClosed()
	{
		for (int i = 0; i < crafting.getSizeInventory(); i++) {
			if(crafting.getStackInSlot(i) != null)
				WorldUtils.dropItem(player.worldObj, crafting.getStackInSlot(i), (int)player.posX, (int)player.posY, (int)player.posZ);
		}
		
		for (int i = 0; i < output.getSizeInventory(); i++) {
			if(output.getStackInSlot(i) != null)
				WorldUtils.dropItem(player.worldObj, output.getStackInSlot(i), (int)player.posX, (int)player.posY, (int)player.posZ);
		}
	}

}
