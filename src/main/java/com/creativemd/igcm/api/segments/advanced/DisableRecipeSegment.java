package com.creativemd.igcm.api.segments.advanced;

import java.util.ArrayList;

import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.gui.controls.gui.GuiStateButton;
import com.creativemd.igcm.api.machine.RecipeMachine;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DisableRecipeSegment extends RecipeSegment<Boolean>{
	
	public boolean showName = false;
	
	public DisableRecipeSegment(String title, Boolean defaultValue, RecipeMachine machine, Object recipe, boolean showName) {
		super(title, defaultValue, machine, recipe);
		this.showName = showName;
	}

	@Override
	public void addSubSegments() {
		ItemStack[] items = new ItemStack[machine.getWidth()*machine.getHeight()];
		machine.fillGrid(items, recipe);
		registerElement("grid", new GridSegment(getKey() + "grid", items, machine).setOffset(30, 0));
		registerElement("result", new GridSegment(getKey() + "result", machine.getOutput(recipe), machine).setOffset(130, 5+machine.getHeight()/2*18));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y, int maxWidth) {
		ArrayList<GuiControl> controls = super.createGuiControls(gui, x, y, maxWidth);
		controls.add(new GuiStateButton(getKey(), value ? 1 : 0, x+150, y+20, 50, 14, "Enabled", "Disabled"));
		if(showName)
			controls.add(new GuiLabel(getKey(), x, y));
		return controls;		
	}
	
	@Override
	public boolean contains(String search) {
		if(super.contains(search))
			return true;
		if(value)
			return "enabled".contains(search);
		return "disabled".contains(search);
	}

	@Override
	public void loadExtra(NBTTagCompound nbt) {
		value = nbt.getBoolean(getKey());
	}

	@Override
	public void saveExtra(NBTTagCompound nbt) {
		if(value)
			nbt.setBoolean(getKey(), value);
		else if(nbt.hasKey(getKey()))
			nbt.removeTag(getKey());
	}

	@Override
	public void saveFromControls() {
		value = !((GuiButton) getGuiControl(getKey())).caption.equals("Enabled");
	}
	
	@Override
	public void set(Boolean newValue) {
		value = newValue;
	}

}
