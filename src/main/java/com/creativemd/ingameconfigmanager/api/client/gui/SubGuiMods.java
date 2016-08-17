package com.creativemd.ingameconfigmanager.api.client.gui;

import java.util.ArrayList;

import com.creativemd.creativecore.client.avatar.AvatarItemStack;
import com.creativemd.creativecore.common.utils.ColorUtils;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.gui.GuiAvatarButton;
import com.creativemd.creativecore.gui.controls.gui.GuiAvatarLabel;
import com.creativemd.creativecore.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.gui.event.gui.GuiControlClickEvent;
import com.creativemd.ingameconfigmanager.api.core.IGCM;
import com.creativemd.ingameconfigmanager.api.core.TabRegistry;
import com.creativemd.ingameconfigmanager.api.tab.ModTab;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class SubGuiMods extends SubGui{
	
	public SubGuiMods() {
		super(250, 250);
	}
	
	@Override
	public void createControls() {
		GuiScrollBox box = new GuiScrollBox("scrollbox", 0, 0, 245, 220);
		ArrayList<ModTab> tabs = TabRegistry.getTabs();
		for (int i = 0; i < tabs.size(); i++) {
			box.addControl(new GuiAvatarButton("" + i, tabs.get(i).title, 5, 5+i*24, 155, 16, tabs.get(i).avatar) {
				@Override
				public void onClicked(int x, int y, int button)
				{
					IGCM.openModOverviewGui(container.player, Integer.parseInt(this.name));
				}
			});
		}
		
		controls.add(box);
		controls.add(new GuiButton("Profiles", 0, 228, 50) {
			@Override
			public void onClicked(int x, int y, int button)
			{
				IGCM.openProfileGui(container.player);
			}
		});
	}

}
