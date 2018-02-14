package com.creativemd.igcm.api.machine;

import java.util.ArrayList;

import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.segments.advanced.DisableRecipeSegment;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class ConfigMachineDisableBranch extends ConfigBranch {
	
	public RecipeMachine machine;
	
	public ArrayList allRecipes;

	public ConfigMachineDisableBranch(String title, RecipeMachine machine) {
		super(title, machine.avatar);
		this.machine = machine;
	}
	
	@Override
	public void initCore()
	{
		allRecipes = new ArrayList(machine.getAllExitingRecipes());
		super.initCore();
	}

	@Override
	public void createChildren() {
		for (int i = 0; i < allRecipes.size(); i++) {
			String recipeName = recipeToString(allRecipes.get(i));
			registerElement(recipeName, new DisableRecipeSegment("recipe", false, machine, allRecipes.get(i), !recipeName.startsWith("{")));
		}
	}

	@Override
	public boolean requiresSynchronization() {
		return true;
	}
	
	public String recipeToString(Object recipe)
	{
		return machine.recipeToString(recipe);
	}
	
	@Override
	public void updateJEI()
	{
		machine.mainBranch.updateJEI();
	}

	@Override
	public void onRecieveFrom(Side side) {
		machine.mainBranch.onRecieveFrom(side);
	}

	@Override
	public void loadExtra(NBTTagCompound nbt) {
		
	}

	@Override
	public void saveExtra(NBTTagCompound nbt) {
		
	}

}
