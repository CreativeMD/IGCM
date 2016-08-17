package com.creativemd.ingameconfigmanager.api.client.gui;

import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.gui.GuiAvatarButton;
import com.creativemd.creativecore.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.gui.controls.gui.GuiScrollBox;
import com.creativemd.ingameconfigmanager.api.core.IGCM;
import com.creativemd.ingameconfigmanager.api.tab.ModTab;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.client.gui.FontRenderer;

public class SubGuiModOverview extends SubGui{
	
	public ModTab tab;
	
	public SubGuiModOverview(ModTab tab) {
		super(250, 250);
		this.tab = tab;
	}

	@Override
	public void createControls() {
		GuiScrollBox box = new GuiScrollBox("scrollbox", 0, 0, 245, 220);
		for (int i = 0; i < tab.branches.size(); i++) {
			box.addControl(new GuiAvatarButton("" + i, tab.branches.get(i).name, 5, 5+i*24, 155, 16, tab.branches.get(i).avatar) {
				@Override
				public void onClicked(int x, int y, int button)
				{
					IGCM.openBranchGui(container.player, tab.branches.get(Integer.parseInt(this.name)));
				}
			});
		}
		
		controls.add(box);
		controls.add(new GuiButton("Back", 0, 228, 50) {
			@Override
			public void onClicked(int x, int y, int button)
			{
				IGCM.openModsGui(container.player);
			}
		});
	}
}
