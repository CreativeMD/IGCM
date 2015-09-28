package com.creativemd.ingameconfigmanager.api.client.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.creativemd.creativecore.common.container.slot.ContainerControl;
import com.creativemd.creativecore.common.container.slot.SlotControl;
import com.creativemd.creativecore.common.container.slot.SlotControlNoSync;
import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.common.gui.controls.GuiButton;
import com.creativemd.creativecore.common.gui.controls.GuiControl;
import com.creativemd.creativecore.common.gui.controls.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.GuiTextfield;
import com.creativemd.creativecore.common.gui.controls.container.GuiSlotControl;
import com.creativemd.creativecore.common.gui.event.ControlChangedEvent;
import com.creativemd.creativecore.common.gui.event.ControlClickEvent;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.utils.stack.StackInfoMaterial;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;
import com.creativemd.ingameconfigmanager.api.common.container.controls.InfoSlotControl;
import com.creativemd.ingameconfigmanager.api.common.packets.BranchInformationPacket;
import com.creativemd.ingameconfigmanager.api.common.packets.RequestInformationPacket;
import com.creativemd.ingameconfigmanager.api.common.segment.ConfigSegment;
import com.creativemd.ingameconfigmanager.api.core.InGameConfigManager;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

public class SubGuiBranch extends SubGui{
	
	public ConfigBranch branch;
	
	public SubGuiBranch(ConfigBranch branch) {
		super(250, 250);
		this.branch = branch;
	}
	
	public static final int loadPerTick = 20;
	public int index;
	public int height;
	public ArrayList<ConfigSegment> segments;
	
	public String search = "";
	
	public void createSegmentControls()
	{
		GuiScrollBox box = (GuiScrollBox) getControl("scrollbox");
		ArrayList<ConfigSegment> segments = branch.getConfigSegments();
		box.gui.controls.clear();
		box.container.controls.clear();
		box.maxScroll = 0;
		box.scrolled = 0;
		
		this.segments = new ArrayList<ConfigSegment>(segments);
		height = 5;
		index = 0;
		onTick();
		
		/*int height = 5;
		for (int i = 0; i < segments.size(); i++) {
			boolean visible = true;
			if(!search.equals(""))
				visible = segments.get(i).contains(search);
			if(visible)
			{
				ArrayList<GuiControl> guiControls = segments.get(i).createGuiControls(this, 0, height, 220);
				ArrayList<ContainerControl> containerControls = segments.get(i).createContainerControls(box.container, 0, height, 220);
				
				segments.get(i).guiControls = guiControls;
				segments.get(i).containerControls = containerControls;
				
				segments.get(i).onSegmentLoaded(0, height, 220);
				
				for (int j = 0; j < guiControls.size(); j++) {
					box.addControl(guiControls.get(j));
				}
				for (int j = 0; j < containerControls.size(); j++) {
					box.addControl(containerControls.get(j));
					guiControls.add(box.gui.controls.get(box.gui.controls.size()-1));
				}
				
				
				height = segments.get(i).getHeight()+5;
			}
			//box.addControl(new GuiAvatarButton(tab.branches.get(i).name, 5, 5+i*20, 155, 20, i, tab.branches.get(i).avatar));
		}
		box.maxScroll += 5;*/
	}
	
	@Override
	public void onTick()
	{
		if(segments != null)
		{
			GuiScrollBox box = (GuiScrollBox) getControl("scrollbox");
			int count = 0;
			int countLoaded = 0;
			for (int i = index; i < segments.size() && countLoaded < loadPerTick; i++) {
				boolean visible = true;
				if(!search.equals(""))
					visible = segments.get(i).contains(search);
				if(visible)
				{
					ArrayList<GuiControl> guiControls = segments.get(i).createGuiControls(this, 0, height, 220);
					ArrayList<ContainerControl> containerControls = segments.get(i).createContainerControls(box.container, 0, height, 220);
					
					segments.get(i).guiControls = guiControls;
					segments.get(i).containerControls = containerControls;
					
					segments.get(i).onSegmentLoaded(0, height, 220);
					
					for (int j = 0; j < guiControls.size(); j++) {
						box.addControl(guiControls.get(j));
					}
					for (int j = 0; j < containerControls.size(); j++) {
						box.addControl(containerControls.get(j));
						guiControls.add(box.gui.controls.get(box.gui.controls.size()-1));
					}
					
					
					height = segments.get(i).getHeight()+5;
					
					countLoaded++;
				}
				
				count++;
				//box.addControl(new GuiAvatarButton(tab.branches.get(i).name, 5, 5+i*20, 155, 20, i, tab.branches.get(i).avatar));
			}
			
			index += count;
			if(index == segments.size()-1)
			{
				box.maxScroll += 5;
				
				segments = null;
				index = 0;
			}
		}
		
	}

	@Override
	public void createControls() {
		GuiScrollBox box = new GuiScrollBox("scrollbox", container.player, 5, 5, 240, 220);
		controls.add(box);
		createSegmentControls();
		controls.add(new GuiButton("Cancel", 5, 226, 40, 20));
		controls.add(new GuiButton("Save", 205, 226, 40, 20));
		if(branch.isSearchable())
			controls.add(new GuiTextfield("search", "", 49, 227, 152, 18));
	}
	
	public GuiSlotControl openedSlot;
	
	@CustomEventSubscribe
	public void onButtonClicked(ControlClickEvent event)
	{
		if(event.source instanceof GuiButton)
		{
			if(event.source.is("Cancel"))
			{
				PacketHandler.sendPacketToServer(new RequestInformationPacket(branch));
				//InGameConfigManager.loadConfig(branch);
				if(branch.tab.branches.size() > 1)
					InGameConfigManager.openModOverviewGui(container.player, branch.tab.getID());
				else if(branch.tab.branches.size() == 1)
					InGameConfigManager.openModsGui(container.player);
			}else if(event.source.is("Save")){
				InGameConfigManager.sendUpdatePacket(branch);
			}
		}else if(event.source instanceof GuiSlotControl){
			if(((GuiSlotControl)event.source).slot instanceof InfoSlotControl)
			{
				openedSlot = (GuiSlotControl) event.source;
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setBoolean("ItemDialog", true);
				nbt.setBoolean("fullEdit", true);
				openNewLayer(nbt);
			}else if(((GuiSlotControl)event.source).slot instanceof SlotControlNoSync)
			{
				openedSlot = (GuiSlotControl) event.source;
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setBoolean("ItemDialog", true);
				nbt.setBoolean("fullEdit", false);
				openNewLayer(nbt);
			}
		}
	}
	
	@Override
	public void onLayerClosed(SubGui gui, NBTTagCompound nbt)
    {
		if(gui instanceof SubGuiFullItemDialog && !nbt.getBoolean("canceled") && openedSlot != null)
		{
			((InfoSlotControl)openedSlot.slot).info = ((SubGuiFullItemDialog) gui).info;
			if(((SubGuiFullItemDialog) gui).info != null)
				openedSlot.slot.slot.putStack(((SubGuiFullItemDialog) gui).info.getItemStack());
			else
				openedSlot.slot.slot.putStack(null);
		}
		if(gui instanceof SubGuiItemDialog && !nbt.getBoolean("canceled") && openedSlot != null)
		{
			openedSlot.slot.slot.putStack(((SubGuiItemDialog) gui).stack);
		}
		openedSlot = null;
    }
	
	@Override
	public SubGui createLayerFromPacket(World world, EntityPlayer player, NBTTagCompound nbt)
    {
		SubGui gui = super.createLayerFromPacket(world, player, nbt);
		if(gui == null && nbt.getBoolean("ItemDialog"))
		{
			if(nbt.getBoolean("fullEdit"))
			{
				SubGuiFullItemDialog dialog = new SubGuiFullItemDialog();
				dialog.info = ((InfoSlotControl)openedSlot.slot).info;
				return dialog;
			}else{
				SubGuiItemDialog dialog = new SubGuiItemDialog();
				dialog.stack = openedSlot.slot.slot.getStack();
				if(dialog.stack != null)
					dialog.stack = dialog.stack.copy();
				return dialog;
			}
		}
		return gui;
    }
	
	
	@CustomEventSubscribe
	public void onTextfieldChanged(ControlChangedEvent event)
	{
		if(event.source instanceof GuiTextfield && event.source.is("search"))
		{
			search = ((GuiTextfield)event.source).text.toLowerCase();
			ArrayList<ConfigSegment> segments = branch.getConfigSegments();
			for (int i = 0; i < segments.size(); i++) {
				segments.get(i).createPacketInformation(false);
			}
			createSegmentControls();
		}
	}
	

	@Override
	public void drawOverlay(FontRenderer fontRenderer) {
		
	}

}
