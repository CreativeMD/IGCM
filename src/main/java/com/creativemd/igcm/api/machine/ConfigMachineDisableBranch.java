package com.creativemd.igcm.api.machine;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.segments.advanced.DisableRecipeSegment;
import com.creativemd.igcm.jei.JEIHandler;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipesArmorDyes;
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
		ItemStack[] input = new ItemStack[machine.getHeight()*machine.getWidth()];
		machine.fillGrid(input, recipe);
		
		boolean emptyRecipe = true;
		StringBuilder builder = new StringBuilder("{");
		for (int i = 0; i < input.length; i++) {
			if(i > 0)
				builder.append(",");
			if(input[i] != null && !input[i].isEmpty())
			{
				try{
					if(input[i].getItem() instanceof ItemBlock)
						builder.append(Block.REGISTRY.getNameForObject(Block.getBlockFromItem(input[i].getItem())).toString());
					else
						builder.append(Item.REGISTRY.getNameForObject(input[i].getItem()).toString());
					builder.append(":" + input[i].getItemDamage());
					emptyRecipe = false;
				}catch(Exception e){
					
				}
			}
		}
		builder.append("}{");
		
		ItemStack[] output = machine.getOutput(recipe);
		for (int i = 0; i < output.length; i++) {
			if(i > 0)
				builder.append(",");
			if(output[i] != null && !output[i].isEmpty())
			{
				try{
					if(output[i].getItem() instanceof ItemBlock)
						builder.append(Block.REGISTRY.getNameForObject(Block.getBlockFromItem(output[i].getItem())).toString());
					else
						builder.append(Item.REGISTRY.getNameForObject(output[i].getItem()).toString());
					builder.append(":" + output[i].getItemDamage());
					emptyRecipe = false;
				}catch(Exception e){
					
				}
			}
		}
		builder.append("}");
		if(emptyRecipe)
			return recipe.getClass().getName();
		return builder.toString();
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
