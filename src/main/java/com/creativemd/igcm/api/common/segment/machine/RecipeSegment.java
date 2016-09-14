package com.creativemd.igcm.api.common.segment.machine;

import java.util.ArrayList;

import com.creativemd.creativecore.gui.ContainerControl;
import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.container.SubContainer;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.igcm.api.common.machine.RecipeMachine;
import com.creativemd.igcm.api.common.segment.ConfigSegment;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		machine.onControlsCreated(!(value instanceof Boolean) ? value : recipe, this instanceof AddRecipeSegment, x, y, maxWidth, guiControls, containerControls);
	}

	@Override
	public ArrayList<ContainerControl> createContainerControls(int x, int y, int maxWidth) {
		ArrayList<ContainerControl> controls = new ArrayList<ContainerControl>();
		for (int i = 0; i < subSegments.size(); i++) {
			ArrayList<ContainerControl> Subcontrols = new ArrayList<ContainerControl>();
			Subcontrols = subSegments.get(i).createContainerControls(x+subSegments.get(i).xOffset, y+subSegments.get(i).yOffset, maxWidth);
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
