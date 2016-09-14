package com.creativemd.igcm.api.common.segment.machine;

import java.util.ArrayList;

import com.creativemd.creativecore.common.utils.stack.StackInfo;
import com.creativemd.creativecore.common.utils.string.StringUtils;
import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.gui.GuiLabel;
import com.creativemd.igcm.api.common.container.controls.GuiButtonRemoveRecipe;
import com.creativemd.igcm.api.common.machine.RecipeMachine;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AddRecipeSegment extends RecipeSegment<Object>{

	public AddRecipeSegment(String id, RecipeMachine machine, Object recipe) {
		super(id, recipe, machine, recipe);
	}
	
	public int width = 0;
	public int height = 0;

	@Override
	public void addSubSegments() {
		StackInfo[] items = null;
		if(value != null)
		{
			items = new StackInfo[machine.getWidth()*machine.getHeight()];
			machine.fillGridInfo(items, recipe);
		}
		addSubSegment(new InfoGridSegment("grid", items, this));
		addSubSegment(new OutputSegment("output", value != null ? machine.getOutput(value) : null, machine).setOffset(114, (machine.getHeight()*18)/2-(machine.getOutputCount()*18)/2));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y, int maxWidth) {
		ArrayList<GuiControl> controls = super.createGuiControls(gui, x, y, maxWidth);
		controls.add(new GuiLabel(getID(), x+maxWidth-110, y+32));
		controls.add(new GuiButtonRemoveRecipe("Remove", x+maxWidth-80, y+30, 70, 14, this));
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
	
	public StackInfo[] getInput(StackInfo[] input)
	{
		int startX = machine.getWidth();
		int endX = 0;
		int startY = machine.getHeight();
		int endY = 0;
		boolean found = false;
		for (int x = 0; x < machine.getWidth(); x++) {
			for (int y = 0; y < machine.getHeight(); y++) {
				if(input[x+y*machine.getWidth()] != null)
				{
					startX = Math.min(startX, x);
					endX = Math.max(endX, x);
					startY = Math.min(startY, y);
					endY = Math.max(endY, y);
					found = true;
				}
			}
		}
		
		int width = endX-startX+1;
		int height = endY-startY+1;
		StackInfo[] result = new StackInfo[width * height];
		for (int i = 0; i < result.length; i++) {
			int rows = i/width;
			result[i] = input[startX+(startY+rows)*machine.getWidth()+(i-rows*width)];
		}
		return result;
	}

	@Override
	public String createPacketInformation(boolean isServer) {
		if(!isServer && guiControls != null)
		{
			for (int i = 0; i < subSegments.size(); i++) {
				subSegments.get(i).createPacketInformation(isServer);
			}
			NBTTagCompound nbt = new NBTTagCompound();
			machine.parseExtraInfo(nbt, this, guiControls, containerControls);
			
			StackInfo[] input = ((InfoGridSegment)subSegments.get(0)).value.clone();
			int startX = machine.getWidth();
			int endX = 0;
			int startY = machine.getHeight();
			int endY = 0;
			boolean found = false;
			for (int x = 0; x < machine.getWidth(); x++) {
				for (int y = 0; y < machine.getHeight(); y++) {
					if(input[x+y*machine.getWidth()] != null)
					{
						startX = Math.min(startX, x);
						endX = Math.max(endX, x);
						startY = Math.min(startY, y);
						endY = Math.max(endY, y);
						found = true;
					}
				}
			}
			
			boolean hasOutput = false;
			if(((OutputSegment)subSegments.get(1)).value != null)
				for (int i = 0; i < ((OutputSegment)subSegments.get(1)).value.length; i++)
					if(((OutputSegment)subSegments.get(1)).value[i] != null)
					{
						hasOutput = true;
						break;
					}
			
			if(found && hasOutput)
			{
				int width = endX-startX+1;
				int height = endY-startY+1;
				StackInfo[] info = new StackInfo[width * height];
				for (int i = 0; i < info.length; i++) {
					int rows = i/width;
					info[i] = input[startX+(startY+rows)*machine.getWidth()+(i-rows*width)];
				}
				this.width = width;
				this.height = height;
				value = machine.parseRecipe(info, ((OutputSegment)subSegments.get(1)).value, nbt, width, height);
			}else
				value = null;
			
		}
		if(value != null)
		{
			StackInfo[] info = new StackInfo[machine.getWidth() * machine.getHeight()];
			machine.fillGridInfo(info, value);
			
			ItemStack[] output = machine.getOutput(value);
			NBTTagCompound nbt = new NBTTagCompound();
			machine.onBeforeSave(value, nbt);
			return StringUtils.ObjectsToString(info, output, nbt, this.width, this.height);
		}
		return null;
	}

	@Override
	public void receivePacketInformation(String input) {
		Object[] objects = StringUtils.StringToObjects(input);
		if(objects.length > 2 && objects[0] instanceof StackInfo[] && objects[1] instanceof ItemStack[] && objects[2] instanceof NBTTagCompound && (objects.length == 3 || objects.length == 4 || objects.length == 5))
		{
			StackInfo[] info = (StackInfo[]) objects[0];
			ItemStack[] output = (ItemStack[]) objects[1];
			NBTTagCompound nbt = (NBTTagCompound) objects[2];
			
			if(objects.length > 4)
				this.width = (Integer) objects[3];
			else
				this.width = machine.getWidth();
			
			if(objects.length == 5)
				this.height = (Integer) objects[4];
			else
				this.height = machine.getHeight();
			
			if(subSegments.size() > 0)
			{
				subSegments.get(0).value = info;
				subSegments.get(1).value = output;
			}
			if(info != null && output != null && nbt != null)
				value = machine.parseRecipe(getInput(info), output, nbt, this.width, this.height);
		}
	}

	@Override
	public boolean contains(String search) {
		return createPacketInformation(false).toLowerCase().contains(search);
	}

}
