package com.creativemd.igcm.api.client.gui;

import java.util.ArrayList;

import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.gui.event.gui.GuiControlClickEvent;
import com.creativemd.igcm.api.core.IGCM;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.client.gui.FontRenderer;

public class SubGuiProfile extends SubGui{
	
	public SubGuiProfile() {
		super(250, 250);
	}

	@Override
	public void createControls() {
		ArrayList<String> lines = new ArrayList<String>();
		for (int i = 0; i < IGCM.profiles.size(); i++) {
			lines.add(IGCM.profiles.get(i));
		}
		GuiComboBox box = new GuiComboBox("profiles", 5, 5, 100, lines);
		box.caption = IGCM.profileName;
		controls.add(box);
		controls.add(new GuiButton("Remove", 120, 5, 40) {
			@Override
			public void onClicked(int x, int y, int button) {
				GuiComboBox combobox = (GuiComboBox) get("profiles");
				if(combobox.lines.size() > 1)
				{
					
					combobox.lines.remove(combobox.caption);
					combobox.caption = combobox.lines.get(0);
				}
			}
		});
		controls.add(new GuiTextfield("Name", "", 5, 40, 100, 16));
		controls.add(new GuiButton("Add", 120, 40, 40) {
			@Override
			public void onClicked(int x, int y, int button) {
				GuiTextfield field = (GuiTextfield) get("name");
				if(!field.text.equals(""))
				{
					GuiComboBox combobox = (GuiComboBox) get("profiles");
					if(!combobox.lines.contains(field.text))
						combobox.lines.add(field.text);
				}
			}
		});
		controls.add(new GuiButton("Cancel", 5, 228, 40) {
			@Override
			public void onClicked(int x, int y, int button) {
				IGCM.openModsGui(container.player);
			}
		});
		controls.add(new GuiButton("Save", 200, 228, 40) {
			@Override
			public void onClicked(int x, int y, int button) {
				GuiComboBox combobox = (GuiComboBox) get("profiles");
				IGCM.profileName = combobox.caption;
				IGCM.profiles = (ArrayList<String>) combobox.lines.clone();
				IGCM.saveProfiles();
				IGCM.loadConfig();
				IGCM.openModsGui(container.player);
			}
		});
	}

}
