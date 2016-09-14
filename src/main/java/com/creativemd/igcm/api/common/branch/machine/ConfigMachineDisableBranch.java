package com.creativemd.igcm.api.common.branch.machine;

import java.util.ArrayList;

import com.creativemd.creativecore.client.avatar.Avatar;
import com.creativemd.creativecore.client.avatar.AvatarItemStack;
import com.creativemd.creativecore.common.utils.string.StringUtils;
import com.creativemd.igcm.api.common.branch.ConfigBranch;
import com.creativemd.igcm.api.common.branch.ConfigSegmentCollection;
import com.creativemd.igcm.api.common.machine.RecipeMachine;
import com.creativemd.igcm.api.common.segment.BooleanSegment;
import com.creativemd.igcm.api.common.segment.machine.DisableRecipeSegment;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ConfigMachineDisableBranch extends ConfigBranch{
	
	public RecipeMachine machine;
	public ArrayList allRecipes;
	//public ArrayList disabledRecipes;
	
	public boolean disableAll = false;
	
	public ConfigMachineDisableBranch(RecipeMachine machine, String name) {
		super(name);
		this.machine = machine;
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
			avatar = getAvatar();
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected Avatar getAvatar() {
		if(machine != null)
			return new AvatarItemStack(machine.getAvatar());
		return null;
	}

	@Override
	public void loadCore() {
		allRecipes = machine.getAllExitingRecipes();
		//disabledRecipes = new ArrayList();
	}

	@Override
	public void createConfigSegments() {
		segments.add(new BooleanSegment("disableAll", "Disable all recipes", false));
		for (int i = 0; i < allRecipes.size(); i++) {
			segments.add(new DisableRecipeSegment(recipeToString(allRecipes.get(i)), true, machine, allRecipes.get(i)));
		}
	}
	
	@Override
	public boolean doesInputSupportStackSize()
	{
		return machine.doesSupportStackSize();
	}
	
	public String recipeToString(Object recipe)
	{
		ItemStack[] input = new ItemStack[machine.getHeight()*machine.getWidth()];
		machine.fillGrid(input, recipe);
		ItemStack[] output = machine.getOutput(recipe);
		return StringUtils.ObjectsToString(input, output);
	}

	@Override
	public boolean needPacket() {
		return true;
	}
	
	@Override
	public void onBeforeReceived(boolean isServer)
	{
		for (int i = 0; i < segments.size(); i++) {
			if(segments.get(i) instanceof DisableRecipeSegment)
				segments.get(i).value = true;
		}
	}

	@Override
	public void onRecieveFrom(boolean isServer, ConfigSegmentCollection collection) {
		machine.clearRecipeList();
		//disabledRecipes.clear();
		
		disableAll = (Boolean) collection.getSegmentValue("disableAll");
		
		if(!disableAll)
		{
			for (int i = 1; i < collection.asList().size(); i++) {
				if(collection.asList().get(i) instanceof DisableRecipeSegment)
				{
					//if(!(Boolean)collection.asList().get(i).value)
						//disabledRecipes.add(((DisableRecipeSegment)collection.asList().get(i)).recipe);
					if((Boolean)collection.asList().get(i).value)
						machine.addRecipeToList(((DisableRecipeSegment)collection.asList().get(i)).recipe);
				}
			}
		}
		
		if(!machine.sendingUpdate && machine.hasAddedBranch())
		{
			machine.sendingUpdate = true;
			machine.addBranch.onRecieveFrom(isServer, new ConfigSegmentCollection(machine.addBranch.getConfigSegments()));
			machine.sendingUpdate = false;
		}
	}

}
