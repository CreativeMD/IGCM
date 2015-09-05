package com.creativemd.ingameconfigmanager.api.common.segment.machine;

import java.util.ArrayList;

import net.minecraft.item.crafting.IRecipe;

import com.creativemd.creativecore.common.container.SubContainer;
import com.creativemd.creativecore.common.container.slot.ContainerControl;
import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.common.gui.controls.GuiControl;
import com.creativemd.ingameconfigmanager.api.common.machine.RecipeMachine;
import com.creativemd.ingameconfigmanager.api.common.segment.ConfigSegment;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class RecipeSegment<T> extends ConfigSegment<T>{
	
	public RecipeMachine machine;
	public Object recipe;
	
	public RecipeSegment(String id, T defaultValue, RecipeMachine machine, Object recipe) {
		super(id, defaultValue);
		this.machine = machine;
		this.recipe = recipe;
		addSubSegments();
	}
	
	public abstract void addSubSegments();
	
	@Override
	public void onSegmentLoaded(int x, int y, int maxWidth)
	{
		machine.onControlsCreated(value, this instanceof AddRecipeSegment, x, y, maxWidth, guiControls, containerControls);
	}

	@Override
	public ArrayList<ContainerControl> createContainerControls(SubContainer gui, int x, int y, int maxWidth) {
		ArrayList<ContainerControl> controls = new ArrayList<ContainerControl>();
		for (int i = 0; i < subSegments.size(); i++) {
			ArrayList<ContainerControl> Subcontrols = new ArrayList<ContainerControl>();
			Subcontrols = subSegments.get(i).createContainerControls(gui, x+subSegments.get(i).xOffset, y+subSegments.get(i).yOffset, maxWidth);
			subSegments.get(i).containerControls = Subcontrols;
			controls.addAll(Subcontrols);
		}
		return controls;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ArrayList<GuiControl> createGuiControls(SubGui gui, int x, int y, int maxWidth) {
		ArrayList<GuiControl> controls = new ArrayList<GuiControl>();
		for (int i = 0; i < subSegments.size(); i++) {
			ArrayList<GuiControl> Subcontrols = new ArrayList<GuiControl>();
			Subcontrols = subSegments.get(i).createGuiControls(gui, x+subSegments.get(i).xOffset, y+subSegments.get(i).yOffset, maxWidth);
			subSegments.get(i).guiControls = Subcontrols;
			controls.addAll(Subcontrols);
		}
		return controls;
	}

}
