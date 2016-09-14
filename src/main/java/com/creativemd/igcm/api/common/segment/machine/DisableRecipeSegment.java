package com.creativemd.igcm.api.common.segment.machine;

import java.util.ArrayList;

import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.gui.controls.gui.GuiStateButton;
import com.creativemd.igcm.api.common.machine.RecipeMachine;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DisableRecipeSegment extends RecipeSegment<Boolean>{

	public DisableRecipeSegment(String id, Boolean defaultValue, RecipeMachine machine, Object recipe) {
		super(id, defaultValue, machine, recipe);
	}

	@Override
	public void addSubSegments() {
		ItemStack[] items = new ItemStack[machine.getWidth()*machine.getHeight()];
		machine.fillGrid(items, recipe);
		addSubSegment(new GridSegment("grid", items, machine).setOffset(30, 0));
		addSubSegment(new GridSegment("result", machine.getOutput(recipe), machine).setOffset(130, 5+machine.getHeight()/2*18));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y, int maxWidth) {
		ArrayList<GuiControl> controls = super.createGuiControls(gui, x, y, maxWidth);
		controls.add(new GuiStateButton("Enabled", value ? 0 : 1, x+150, y+20, 50, 14, "Enabled", "Disabled"));
		return controls;		
	}

	@Override
	public String createPacketInformation(boolean isServer) {
		if(!isServer && guiControls != null)
			for (int i = 0; i < guiControls.size(); i++)
				if(guiControls.get(i) instanceof GuiButton)
					value = ((GuiButton)guiControls.get(i)).caption.equals("Enabled");
		if(!value)
			return "false";
		return null;
	}

	@Override
	public void receivePacketInformation(String input) {
		value = false;
	}
	
	@Override
	public boolean contains(String search) {
		ItemStack[] items = new ItemStack[machine.getWidth()*machine.getHeight()];
		machine.fillGrid(items, recipe);
		for (int i = 0; i < items.length; i++) {
			if(items[i] != null)
				if(items[i].getItem() instanceof ItemBlock)
					if(Block.REGISTRY.getNameForObject(Block.getBlockFromItem(items[i].getItem())).toString().toLowerCase().contains(search))
						return true;
				else
					if(Item.REGISTRY.getNameForObject(items[i].getItem()).toString().toLowerCase().contains(search))
						return true;
		}
		
		if(getID().toLowerCase().contains(search))
			return true;
		if(value)
			return "enabled".contains(search);
		return "disabled".contains(search);
	}

}
