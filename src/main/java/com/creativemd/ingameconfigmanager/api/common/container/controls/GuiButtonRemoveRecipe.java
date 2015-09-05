package com.creativemd.ingameconfigmanager.api.common.container.controls;

import com.creativemd.creativecore.common.gui.controls.GuiButton;
import com.creativemd.ingameconfigmanager.api.common.segment.machine.AddRecipeSegment;

public class GuiButtonRemoveRecipe extends GuiButton{
	
	public AddRecipeSegment segment;
	
	public GuiButtonRemoveRecipe(String caption, int x, int y, int width, int height, AddRecipeSegment segment) {
		super(caption, x, y, width, height);
		this.segment = segment;
	}
	
	@Override
	public boolean mousePressed(int posX, int posY, int button)
	{
		if(super.mousePressed(posX, posY, button))
		{
			segment.onRemoveRecipe();
			return true;
		}
		return false;
	}
	
}
