package com.creativemd.igcm.api.common.container.controls;

import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.GuiRenderHelper;
import com.creativemd.creativecore.gui.client.style.Style;
import com.creativemd.creativecore.gui.controls.gui.GuiButton;
import com.creativemd.igcm.api.common.segment.machine.AddRecipeSegment;

public class GuiButtonRemoveRecipe extends GuiButton {
	
	public AddRecipeSegment segment;

	public GuiButtonRemoveRecipe(String caption, int x, int y, int width, int height, AddRecipeSegment segment) {
		super(caption, x, y, width, height);
		this.segment = segment;
	}

	@Override
	public void onClicked(int x, int y, int button) {
		segment.onRemoveRecipe();
	}

}
