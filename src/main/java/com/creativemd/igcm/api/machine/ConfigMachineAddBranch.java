package com.creativemd.igcm.api.machine;

import java.util.ArrayList;

import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.gui.controls.gui.GuiScrollBox;
import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.ConfigSegment;
import com.creativemd.igcm.api.segments.advanced.AddRecipeSegment;
import com.creativemd.igcm.client.gui.SubGuiConfigSegement;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ConfigMachineAddBranch extends ConfigBranch {
	
	public RecipeMachine machine;

	public ConfigMachineAddBranch(String title, RecipeMachine machine) {
		super(title, machine.avatar);
		this.machine = machine;
	}

	@Override
	public void createChildren() {
		
	}

	@Override
	public boolean requiresSynchronization() {
		return true;
	}
	
	@Override
	public void initDefault() {
		recipes.clear();
		super.initDefault();
	}
	
	public ArrayList<AddRecipeSegment> recipes = new ArrayList<>();
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onGuiCreatesSegments(SubGuiConfigSegement gui, ArrayList<ConfigSegment> segments)
	{
		segments.addAll(recipes);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onGuiLoadedAllSegments(SubGuiConfigSegement gui, GuiScrollBox box, ArrayList<ConfigSegment> segments)
	{
		box.addControl(new GuiButton("add recipe", 5, gui.height) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				box.controls.remove(this);
				segments.add(new AddRecipeSegment("", machine, null));
			}
		});
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onGuiLoadSegment(SubGuiConfigSegement gui, GuiScrollBox box, ArrayList<ConfigSegment> segments, ConfigSegment segment)
	{
		GuiButton button = new GuiButton("X", 200, gui.height, 14) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				gui.removeSegment(segment);
			}
		};
		
		segment.getGuiControls().add(button);
		box.addControl(button);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onGuiSavesSegments(SubGuiConfigSegement gui, ArrayList<ConfigSegment> segments)
	{
		for (int i = 0; i < segments.size(); i++) {
			segments.get(i).saveFromControls();
		}
		
		recipes.clear();
		
		for (int i = 0; i < segments.size(); i++) {
			ConfigSegment segment = segments.get(i);
			if(segment instanceof AddRecipeSegment && ((AddRecipeSegment) segment).value != null)
				recipes.add((AddRecipeSegment) segment);
		}
		
		machine.onRecipeParsed(recipes);
	}
	
	@Override
	public boolean doesInputSupportStackSize()
	{
		return machine.doesSupportStackSize();
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
		NBTTagList list = nbt.getTagList("added", 10);
		recipes = new ArrayList<>();
		for (int i = 0; i < list.tagCount(); i++) {
			AddRecipeSegment recipe = new AddRecipeSegment("", machine, null);
			recipe.load(list.getCompoundTagAt(i));
			if(recipe.value != null)
				recipes.add(recipe);
		}
	}

	@Override
	public void saveExtra(NBTTagCompound nbt) {
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < recipes.size(); i++) {
			NBTTagCompound recipeNBT = new NBTTagCompound();
			recipes.get(i).save(recipeNBT);
			list.appendTag(recipeNBT);
		}
		nbt.setTag("added", list);
	}

}
