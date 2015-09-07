package com.creativemd.ingameconfigmanager.api.common.segment.machine;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.common.gui.controls.GuiControl;
import com.creativemd.creativecore.common.gui.controls.GuiStateButton;
import com.creativemd.creativecore.common.recipe.entry.BetterShapelessRecipe;
import com.creativemd.creativecore.common.utils.stack.StackInfo;
import com.creativemd.creativecore.common.utils.string.StringUtils;
import com.creativemd.ingameconfigmanager.api.common.container.controls.GuiButtonRemoveRecipe;
import com.creativemd.ingameconfigmanager.api.common.machine.RecipeMachine;
import com.ibm.icu.util.Output;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AddRecipeSegment extends RecipeSegment<Object>{

	public AddRecipeSegment(String id, RecipeMachine machine, Object recipe) {
		super(id, recipe, machine, recipe);
	}

	@Override
	public void addSubSegments() {
		addSubSegment(new InfoGridSegment("grid", value != null ? machine.fillGridInfo(value) : null, this));
		addSubSegment(new OutputSegment("output", value != null ? machine.getOutput(value) : null, machine).setOffset(100, machine.getHeight()/2*18));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y, int maxWidth) {
		ArrayList<GuiControl> controls = super.createGuiControls(gui, x, y, maxWidth);
		controls.add(new GuiButtonRemoveRecipe("Remove", x+maxWidth-80, y+30, 70, 20, this));
		return controls;
	}
	
	public void onRemoveRecipe()
	{
		if(subSegments.size() > 0)
		{
			((InfoGridSegment)subSegments.get(0)).empty();
			((OutputSegment)subSegments.get(1)).empty();
		}
	}

	@Override
	public String createPacketInformation() {
		if(guiControls != null)
		{
			for (int i = 0; i < subSegments.size(); i++) {
				subSegments.get(i).createPacketInformation();
			}
			NBTTagCompound nbt = new NBTTagCompound();
			machine.parseExtraInfo(nbt, this, guiControls, containerControls);
			value = machine.parseRecipe(((InfoGridSegment)subSegments.get(0)).value, ((OutputSegment)subSegments.get(1)).value, nbt);
		}
		if(value != null)
		{
			StackInfo[] info = machine.fillGridInfo(value);
			ItemStack[] output = machine.getOutput(value);
			NBTTagCompound nbt = new NBTTagCompound();
			machine.onBeforeSave(value, nbt);
			return StringUtils.ObjectsToString(info, output, nbt);
		}
		return null;
	}

	@Override
	public void receivePacketInformation(String input) {
		Object[] objects = StringUtils.StringToObjects(input);
		if(objects.length == 3)
		{
			StackInfo[] info = (StackInfo[]) objects[0];
			ItemStack[] output = (ItemStack[]) objects[1];
			NBTTagCompound nbt = (NBTTagCompound) objects[2];
			if(subSegments.size() > 0)
			{
				subSegments.get(0).value = info;
				subSegments.get(1).value = output;
			}
			value = machine.parseRecipe(info, output, nbt);
		}
	}

	@Override
	public boolean contains(String search) {
		return createPacketInformation().toLowerCase().contains(search);
	}

}
