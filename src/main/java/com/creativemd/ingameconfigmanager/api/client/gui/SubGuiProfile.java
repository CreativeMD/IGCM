package com.creativemd.ingameconfigmanager.api.client.gui;

import java.util.ArrayList;


import net.minecraft.client.gui.FontRenderer;

import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.common.gui.controls.GuiButton;
import com.creativemd.creativecore.common.gui.controls.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.GuiListBox;
import com.creativemd.creativecore.common.gui.controls.GuiTextfield;
import com.creativemd.creativecore.common.gui.event.ControlClickEvent;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;
import com.creativemd.ingameconfigmanager.api.core.InGameConfigManager;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

public class SubGuiProfile extends SubGui{
	
	public SubGuiProfile() {
		super(250, 250);
	}

	@Override
	public void createControls() {
		ArrayList<String> lines = new ArrayList<String>();
		for (int i = 0; i < InGameConfigManager.profiles.size(); i++) {
			lines.add(InGameConfigManager.profiles.get(i));
		}
		GuiComboBox box = new GuiComboBox("profiles", 5, 5, 100, lines);
		box.caption = InGameConfigManager.profileName;
		controls.add(box);
		controls.add(new GuiButton("Remove", 120, 5, 40, 20));
		controls.add(new GuiTextfield("Name", "", 5, 40, 100, 20));
		controls.add(new GuiButton("Add", 120, 40, 40, 20));
		controls.add(new GuiButton("Cancel", 5, 226, 40, 20));
		controls.add(new GuiButton("Save", 205, 226, 40, 20));
	}
	
	@CustomEventSubscribe
	public void onButtonClicked(ControlClickEvent event)
	{
		if(event.source instanceof GuiButton)
		{
			if(event.source.is("Cancel"))
			{
				InGameConfigManager.openModsGui(container.player);
			}else if(event.source.is("Save")){
				GuiComboBox combobox = (GuiComboBox) getControl("profiles");
				InGameConfigManager.profileName = combobox.caption;
				InGameConfigManager.profiles = (ArrayList<String>) combobox.lines.clone();
				InGameConfigManager.saveProfiles();
				InGameConfigManager.loadConfig();
				InGameConfigManager.openModsGui(container.player);
			}else if(event.source.is("remove"))
			{
				if(InGameConfigManager.profiles.size() > 1)
				{
					GuiComboBox combobox = (GuiComboBox) getControl("profiles");
					combobox.lines.remove(combobox.caption);
					combobox.caption = combobox.lines.get(0);
				}
			}else if(event.source.is("add"))
			{
				GuiTextfield field = (GuiTextfield) getControl("name");
				if(!field.text.equals(""))
				{
					GuiComboBox combobox = (GuiComboBox) getControl("profiles");
					combobox.lines.add(field.text);
				}
			}
		}
	}
	
	@Override
	public void drawOverlay(FontRenderer fontRenderer) {
		
	}

}
