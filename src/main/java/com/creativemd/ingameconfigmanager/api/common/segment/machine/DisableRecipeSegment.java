package com.creativemd.ingameconfigmanager.api.common.segment.machine;

import java.util.ArrayList;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

import com.creativemd.creativecore.common.container.SubContainer;
import com.creativemd.creativecore.common.container.slot.ContainerControl;
import com.creativemd.creativecore.common.container.slot.SlotControlNoSync;
import com.creativemd.creativecore.common.container.slot.SlotPreview;
import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.common.gui.controls.GuiButton;
import com.creativemd.creativecore.common.gui.controls.GuiControl;
import com.creativemd.creativecore.common.gui.controls.GuiStateButton;
import com.creativemd.ingameconfigmanager.api.common.machine.RecipeMachine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DisableRecipeSegment extends RecipeSegment<Boolean>{

	public DisableRecipeSegment(String id, Boolean defaultValue, RecipeMachine machine, Object recipe) {
		super(id, defaultValue, machine, recipe);
	}

	@Override
	public void addSubSegments() {
		addSubSegment(new GridSegment("grid", machine.fillGrid(recipe), machine).setOffset(30, 5));
		addSubSegment(new GridSegment("result", machine.getOutput(recipe), machine).setOffset(90, 5+18));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y, int maxWidth) {
		ArrayList<GuiControl> controls = super.createGuiControls(gui, x, y, maxWidth);
		controls.add(new GuiStateButton("Enabled", value ? 0 : 1, x+120, y+20, 50, 20, "Enabled", "Disabled"));
		return controls;		
	}

	@Override
	public String createPacketInformation() {
		if(guiControls != null)
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
		if(getID().toLowerCase().contains(search))
			return true;
		if(value)
			return "enabled".contains(search);
		return "disabled".contains(search);
	}

}
