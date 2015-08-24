package com.creativemd.ingameconfigmanager.api.client.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.FontRenderer;

import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.common.gui.controls.GuiAvatarButton;
import com.creativemd.creativecore.common.gui.controls.GuiButton;
import com.creativemd.creativecore.common.gui.controls.GuiScrollBox;
import com.creativemd.ingameconfigmanager.api.core.TabRegistry;
import com.creativemd.ingameconfigmanager.api.tab.ModTab;

public class SubGuiModOverview extends SubGui{
	
	public ModTab tab;
	
	public SubGuiModOverview(ModTab tab) {
		super(250, 250);
		this.tab = tab;
	}

	@Override
	public void createControls() {
		GuiScrollBox box = new GuiScrollBox("scrollbox", container.player, 5, 5, 240, 220);
		for (int i = 0; i < tab.branches.size(); i++) {
			box.addControl(new GuiAvatarButton(tab.branches.get(i).name, 5, 5+i*20, 155, 20, i, tab.branches.get(i).avatar));
		}
		
		controls.add(box);
	}

	@Override
	public void drawOverlay(FontRenderer fontRenderer) {
		
	}
}
