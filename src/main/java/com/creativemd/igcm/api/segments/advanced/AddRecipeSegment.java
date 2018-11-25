package com.creativemd.igcm.api.segments.advanced;

import java.util.ArrayList;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.utils.stack.InfoStack;
import com.creativemd.igcm.api.machine.RecipeMachine;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AddRecipeSegment extends RecipeSegment<Object> {
	
	public AddRecipeSegment(String id, RecipeMachine machine, Object recipe) {
		super(id, recipe, machine, recipe);
	}
	
	public int width = 0;
	public int height = 0;
	
	@Override
	public void addSubSegments() {
		InfoStack[] items = null;
		if (value != null) {
			items = new InfoStack[machine.getWidth() * machine.getHeight()];
			machine.fillGridInfo(items, recipe);
		}
		registerElement("grid", new InfoGridSegment("grid", items, machine));
		registerElement("output", new OutputSegment("output", value != null ? machine.getOutput(value) : null, machine).setOffset(114, (machine.getHeight() * 18) / 2 - (machine.getOutputCount() * 18) / 2));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y, int maxWidth) {
		/* ArrayList<GuiControl> controls = super.createGuiControls(gui, x, y, maxWidth);
		 * controls.add(new GuiLabel(getKey(), x+maxWidth-110, y+32));
		 * return controls; */
		return super.createGuiControls(gui, x, y, maxWidth);
	}
	
	public void onRemoveRecipe() {
		((InfoGridSegment) getChildByKey("grid")).empty();
		((OutputSegment) getChildByKey("output")).empty();
	}
	
	public InfoStack[] getInput(InfoStack[] input) {
		int startX = machine.getWidth();
		int endX = 0;
		int startY = machine.getHeight();
		int endY = 0;
		boolean found = false;
		for (int x = 0; x < machine.getWidth(); x++) {
			for (int y = 0; y < machine.getHeight(); y++) {
				if (input[x + y * machine.getWidth()] != null) {
					startX = Math.min(startX, x);
					endX = Math.max(endX, x);
					startY = Math.min(startY, y);
					endY = Math.max(endY, y);
					found = true;
				}
			}
		}
		
		int width = endX - startX + 1;
		int height = endY - startY + 1;
		InfoStack[] result = new InfoStack[width * height];
		for (int i = 0; i < result.length; i++) {
			int rows = i / width;
			result[i] = input[startX + (startY + rows) * machine.getWidth() + (i - rows * width)];
		}
		return result;
	}
	
	public void parseRecipe(NBTTagCompound nbt) {
		InfoStack[] input = ((InfoGridSegment) getChildByKey("grid")).value.clone();
		int startX = machine.getWidth();
		int endX = 0;
		int startY = machine.getHeight();
		int endY = 0;
		boolean found = false;
		for (int x = 0; x < machine.getWidth(); x++) {
			for (int y = 0; y < machine.getHeight(); y++) {
				if (input[x + y * machine.getWidth()] != null) {
					startX = Math.min(startX, x);
					endX = Math.max(endX, x);
					startY = Math.min(startY, y);
					endY = Math.max(endY, y);
					found = true;
				}
			}
		}
		
		boolean hasOutput = false;
		OutputSegment output = (OutputSegment) getChildByKey("output");
		if (output.value != null)
			for (int i = 0; i < output.value.length; i++)
				if (output.value[i] != null && !output.value[i].isEmpty()) {
					hasOutput = true;
					break;
				}
			
		if (found && hasOutput) {
			int width = endX - startX + 1;
			int height = endY - startY + 1;
			InfoStack[] info = new InfoStack[width * height];
			for (int i = 0; i < info.length; i++) {
				int rows = i / width;
				info[i] = input[startX + (startY + rows) * machine.getWidth() + (i - rows * width)];
			}
			this.width = width;
			this.height = height;
			value = machine.parseRecipe(info, output.value, nbt, width, height);
		} else
			value = null;
	}
	
	@Override
	public void loadExtra(NBTTagCompound nbt) {
		parseRecipe(nbt);
	}
	
	@Override
	public void saveExtra(NBTTagCompound nbt) {
		machine.writeExtraInfo(value, nbt);
	}
	
	@Override
	public void saveFromControls() {
		super.saveFromControls();
		NBTTagCompound nbt = new NBTTagCompound();
		machine.parseExtraInfo(nbt, this, getGuiControls(), getContainerControls());
		
		parseRecipe(nbt);
	}
	
	@Override
	public void set(Object newValue) {
		value = newValue;
	}
	
}
