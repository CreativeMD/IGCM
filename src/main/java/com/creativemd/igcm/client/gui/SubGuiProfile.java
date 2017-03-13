package com.creativemd.igcm.client.gui;

import java.util.ArrayList;

import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.gui.controls.gui.GuiTextfield;
import com.creativemd.igcm.IGCM;
import com.creativemd.igcm.IGCMConfig;
import com.creativemd.igcm.IGCMGuiManager;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class SubGuiProfile extends SubGui{
	
	public SubGuiProfile() {
		super(250, 250);
	}

	@Override
	public void createControls() {
		ArrayList<String> lines = new ArrayList<String>();
		for (int i = 0; i < IGCMConfig.profiles.size(); i++) {
			lines.add(IGCMConfig.profiles.get(i));
		}
		GuiComboBox box = new GuiComboBox("profiles", 5, 5, 100, lines);
		box.caption = IGCMConfig.profileName;
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
				IGCMGuiManager.openConfigGui(getPlayer());
			}
		});
		controls.add(new GuiButton("Save", 200, 228, 40) {
			@Override
			public void onClicked(int x, int y, int button) {
				GuiComboBox combobox = (GuiComboBox) get("profiles");
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("profile", combobox.caption);
				NBTTagList list = new NBTTagList();
				for (int i = 0; i < combobox.lines.size(); i++) {
					list.appendTag(new NBTTagString(combobox.lines.get(i)));
				}
				nbt.setTag("profiles", list);
				sendPacketToServer(nbt);
			}
		});
	}

}