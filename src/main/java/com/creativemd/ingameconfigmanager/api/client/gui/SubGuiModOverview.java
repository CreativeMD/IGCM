package com.creativemd.ingameconfigmanager.api.client.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.FontRenderer;

import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.common.gui.controls.GuiAvatarButton;
import com.creativemd.creativecore.common.gui.controls.GuiButton;
import com.creativemd.creativecore.common.gui.controls.GuiScrollBox;
import com.creativemd.creativecore.common.gui.event.ControlClickEvent;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;
import com.creativemd.ingameconfigmanager.api.core.InGameConfigManager;
import com.creativemd.ingameconfigmanager.api.core.TabRegistry;
import com.creativemd.ingameconfigmanager.api.tab.ModTab;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

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
		controls.add(new GuiButton("Back", 5, 226, 50, 20));
	}
	
	@CustomEventSubscribe
	public void onButtonClicked(ControlClickEvent event)
	{
		if(event.source instanceof GuiButton)
		{
			if(((GuiButton)event.source).caption.equals("Back"))
				InGameConfigManager.openModsGui(container.player);
			else
				InGameConfigManager.openBranchGui(container.player, tab.branches.get(((GuiButton)event.source).id));
		}
	}

	@Override
	public void drawOverlay(FontRenderer fontRenderer) {
		
	}
}
