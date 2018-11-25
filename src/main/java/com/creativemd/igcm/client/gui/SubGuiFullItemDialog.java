package com.creativemd.igcm.client.gui;

import java.util.ArrayList;

import com.creativemd.creativecore.client.avatar.AvatarItemStack;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiAvatarLabelClickable;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.creativemd.creativecore.common.gui.event.gui.GuiToolTipEvent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.creativecore.common.utils.stack.InfoStack;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.nbt.NBTTagCompound;

public class SubGuiFullItemDialog extends SubGui {
	
	public static ArrayList<InfoStack> latest = new ArrayList<InfoStack>();
	
	public InfoStack info;
	
	public boolean supportStackSize;
	
	public SubGuiFullItemDialog(boolean supportStackSize) {
		super(150, 230);
		this.supportStackSize = supportStackSize;
	}
	
	public GuiInfoHandler handler;
	
	@Override
	public void createControls() {
		
		handler = GuiInfoHandler.getHandler(info);
		
		GuiComboBox box = (GuiComboBox) get("type");
		if (box != null)
			handler = GuiInfoHandler.getHandler(box.caption);
		
		controls.clear();
		ArrayList<String> lines = new ArrayList<String>(GuiInfoHandler.getNames());
		//lines.add("Latest");
		
		box = new GuiComboBox("type", 0, 0, 144, lines);
		box.caption = handler.getName();
		box.index = lines.indexOf(handler.getName());
		controls.add(box);
		
		handler.createControls(this, info);
		
		if (supportStackSize) {
			controls.add(new GuiLabel("StackSize:", 5, 210));
			GuiTextfield field = new GuiTextfield("stacksize", "1", 110, 208, 30, 14).setNumbersOnly();
			if (info != null)
				field.text = "" + info.stackSize;
			controls.add(field);
		}
		
		GuiScrollBox scroll = new GuiScrollBox("latest", 0, 155, 144, supportStackSize ? 45 : 65);
		int latestPerRow = 4;
		for (int i = 0; i < latest.size(); i++) {
			int row = i / latestPerRow;
			int cell = i - (row * latestPerRow);
			
			GuiAvatarLabelClickable avatar = new GuiAvatarLabelClickable("" + i, cell * 32, row * 18, ColorUtils.WHITE, new AvatarItemStack(latest.get(i).getItemStack())) {
				
				@Override
				public void onClicked(int x, int y, int button) {
					info = latest.get(Integer.parseInt(name));
					closeLayer(new NBTTagCompound());
				}
			};
			scroll.addControl(avatar);
		}
		controls.add(scroll);
		
		controls.add(new GuiButton("Cancel", 0, 130, 41) {
			
			@Override
			public void onClicked(int x, int y, int button) {
			}
		});
		controls.add(new GuiButton("Remove", 50, 130, 41) {
			
			@Override
			public void onClicked(int x, int y, int button) {
			}
		});
		controls.add(new GuiButton("Save", 100, 130, 41) {
			
			@Override
			public void onClicked(int x, int y, int button) {
			}
		});
	}
	
	@CustomEventSubscribe
	public void onClicked(GuiControlClickEvent event) {
		if (event.source.is("Save")) {
			int stackSize = 0;
			try {
				if (supportStackSize)
					stackSize = Integer.parseInt(((GuiTextfield) get("stacksize")).text);
				else
					stackSize = 1;
			} catch (Exception e) {
				stackSize = 1;
			}
			
			InfoStack info = handler.parseInfo(this, stackSize);
			if (info != null) {
				this.info = info;
				if (!latest.contains(info))
					latest.add(0, info.copy());
				closeLayer(new NBTTagCompound());
			}
		}
		if (event.source.is("Remove")) {
			this.info = null;
			closeLayer(new NBTTagCompound());
		}
		if (event.source.is("Cancel")) {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("canceled", true);
			closeLayer(nbt);
		}
	}
	
	@CustomEventSubscribe
	public void onToolTip(GuiToolTipEvent event) {
		if (event.source.is("stacksize")) {
			event.tooltip.add("0: no consumption");
			event.tooltip.add("1: normal");
		}
	}
	
	@CustomEventSubscribe
	public void onChanged(GuiControlChangedEvent event) {
		if (event.source.is("type")) {
			createControls();
			refreshControls();
		} else
			handler.onChanged(this, event);
	}
	
}
