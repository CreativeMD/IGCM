package com.creativemd.ingameconfigmanager.api.client.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;

import com.creativemd.creativecore.common.container.slot.ContainerControl;
import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.common.gui.controls.GuiButton;
import com.creativemd.creativecore.common.gui.controls.GuiControl;
import com.creativemd.creativecore.common.gui.controls.GuiScrollBox;
import com.creativemd.creativecore.common.gui.event.ControlClickEvent;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;
import com.creativemd.ingameconfigmanager.api.common.packets.BranchInformationPacket;
import com.creativemd.ingameconfigmanager.api.common.segment.ConfigSegment;
import com.creativemd.ingameconfigmanager.api.core.InGameConfigManager;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

public class SubGuiBranch extends SubGui{
	
	public ConfigBranch branch;
	
	public SubGuiBranch(ConfigBranch branch) {
		super(250, 250);
		this.branch = branch;
	}

	@Override
	public void createControls() {
		GuiScrollBox box = new GuiScrollBox("scrollbox", container.player, 5, 5, 240, 220);
		ArrayList<ConfigSegment> segments = branch.getConfigSegments();
		int height = 5;
		for (int i = 0; i < segments.size(); i++) {
			ArrayList<GuiControl> guiControls = segments.get(i).createGuiControls(this, 0, height, 220);
			ArrayList<ContainerControl> containerControls = segments.get(i).createContainerControls(box.container, 0, height, 220);
			for (int j = 0; j < guiControls.size(); j++) {
				box.addControl(guiControls.get(j));
			}
			for (int j = 0; j < containerControls.size(); j++) {
				box.addControl(containerControls.get(j));
			}
			segments.get(i).guiControls = guiControls;
			segments.get(i).containerControls = containerControls;
			height = segments.get(i).getHeight();
			//box.addControl(new GuiAvatarButton(tab.branches.get(i).name, 5, 5+i*20, 155, 20, i, tab.branches.get(i).avatar));
		}
		
		controls.add(box);
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
				if(branch.tab.branches.size() > 1)
					InGameConfigManager.openModOverviewGui(container.player, 0);
				else if(branch.tab.branches.size() == 1)
					InGameConfigManager.openModsGui(container.player);
			}else if(event.source.is("Save")){
				InGameConfigManager.sendUpdatePacket(branch);
			}
		}
	}

	@Override
	public void drawOverlay(FontRenderer fontRenderer) {
		
	}

}
