package com.creativemd.ingameconfigmanager.api.client.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.FontRenderer;

import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.common.gui.controls.GuiAvatarButton;
import com.creativemd.creativecore.common.gui.controls.GuiScrollBox;
import com.creativemd.ingameconfigmanager.api.core.TabRegistry;
import com.creativemd.ingameconfigmanager.api.tab.ModTab;

public class SubGuiMods extends SubGui{
	
	@Override
	public void createControls() {
		GuiScrollBox box = new GuiScrollBox(155/2+10, 145/2+10, 155, 145);
		ArrayList<ModTab> tabs = TabRegistry.getTabs();
		for (int i = 0; i < tabs.size(); i++) {
			box.addControl(new GuiAvatarButton(tabs.get(i).title, 20+i*35, 15, 30, 20, i, tabs.get(i).avatar));
		}
		controls.add(box);
	}

	@Override
	public void drawOverlay(FontRenderer fontRenderer) {
		
	}

}
