package com.creativemd.igcm.api.machine;

import java.util.Collection;
import java.util.Iterator;

import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.ConfigSegment;
import com.creativemd.igcm.api.segments.SelectSegment;
import com.creativemd.igcm.api.segments.advanced.AddRecipeSegment;
import com.creativemd.igcm.api.segments.advanced.DisableRecipeSegment;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class ConfigMachineBranch<T> extends ConfigBranch {
	
	public static String[] states = new String[]{"all", "addedonly", "noadded", "none"};
	
	public RecipeMachine<T> machine;
	
	public int state;
	
	public ConfigMachineBranch(String title, ItemStack avatar, RecipeMachine<T> machine) {
		super(title, avatar);
		this.state = 0;
		this.machine = machine;		
	}

	@Override
	public void createChildren() {
		if(machine.hasDisableBranch())
		{
			machine.disableBranch = new ConfigMachineDisableBranch(title + " Disable", machine);
			registerElement(getKey() + "_disable", machine.disableBranch);
		}
		
		if(machine.hasAddedBranch())
		{
			machine.addBranch = new ConfigMachineAddBranch(title + " Add", machine);
			registerElement(getKey() + "_add", machine.addBranch);
		}
		
		registerElement("state", new SelectSegment("General", state, states));
		
		machine.createExtraSegments();
	}

	@Override
	public boolean requiresSynchronization() {
		return true;
	}

	@Override
	public void onRecieveFrom(Side side) {
		machine.clearRecipeList(side);
		//disabledRecipes.clear();
		
		SelectSegment select = (SelectSegment) getChildByKey("state");
		int mode = select.getIndex();
		
		if(machine.hasDisableBranch())
		{
			if(mode == 0 || mode == 2)
			{
				Collection<ConfigSegment> segments = machine.disableBranch.getChilds();
				for (Iterator iterator = segments.iterator(); iterator.hasNext();) {
					ConfigSegment segment = (ConfigSegment) iterator.next();
					if(segment instanceof DisableRecipeSegment)
					{
						//if(!(Boolean)collection.asList().get(i).value)
							//disabledRecipes.add(((DisableRecipeSegment)collection.asList().get(i)).recipe);
						if(!((DisableRecipeSegment) segment).value)
							machine.addRecipeToList(side, (T) ((DisableRecipeSegment)segment).recipe);
					}
				}
			}
		}
		
		if(machine.hasAddedBranch())
		{
			if(mode == 0 || mode == 1)
			{
				for (Iterator iterator = machine.addBranch.recipes.iterator(); iterator.hasNext();) {
					ConfigSegment segment = (ConfigSegment) iterator.next();
					if(segment instanceof AddRecipeSegment && ((AddRecipeSegment) segment).value != null)
					{
						machine.addRecipeToList(side, (T) ((AddRecipeSegment) segment).value);
					}
				}
			}
		}
		
		machine.onReceiveFrom(side);
	}
	
	@Override
	public void updateJEI() {
		machine.updateJEI();
	}

	@Override
	public boolean doesInputSupportStackSize() {
		return machine.doesSupportStackSize();
	}

	@Override
	public void loadExtra(NBTTagCompound nbt) {
		state = nbt.getInteger("state");
	}

	@Override
	public void saveExtra(NBTTagCompound nbt) {
		nbt.setInteger("state", state);
	}
	
	@Override
	public void onUpdateSendToClient(EntityPlayer player) {
		machine.onUpdateSendToClient(player);
	}
}
