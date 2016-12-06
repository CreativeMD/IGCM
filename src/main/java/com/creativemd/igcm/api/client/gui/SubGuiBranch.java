package com.creativemd.igcm.api.client.gui;

import java.util.ArrayList;

import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.gui.ContainerControl;
import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.container.SubContainer;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.container.SlotControlNoSync;
import com.creativemd.creativecore.gui.controls.container.client.GuiSlotControl;
import com.creativemd.creativecore.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.gui.event.gui.GuiControlClickEvent;
import com.creativemd.igcm.api.common.branch.ConfigBranch;
import com.creativemd.igcm.api.common.branch.ConfigSegmentCollection;
import com.creativemd.igcm.api.common.container.controls.InfoSlotControl;
import com.creativemd.igcm.api.common.packets.RequestInformationPacket;
import com.creativemd.igcm.api.common.segment.ConfigSegment;
import com.creativemd.igcm.api.core.IGCM;
import com.creativemd.igcm.api.jei.JEIHandler;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

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
		GuiScrollBox box = (GuiScrollBox) get("scrollbox");
		ArrayList<ConfigSegment> segments = branch.getConfigSegments();
		box.controls.clear();
		box.maxScroll = 0;
		box.scrolled = 0;
		
		if(get("Save") != null)
			get("Save").setEnabled(false);
		
		this.segments = new ArrayList<ConfigSegment>(segments);
		height = 5;
		index = 0;
		onTick();
	}
	
	@Override
	public void onTick()
	{
		if(segments != null)
		{
			GuiScrollBox box = (GuiScrollBox) get("scrollbox");
			int count = 0;
			int countLoaded = 0;
			for (int i = index; i < segments.size() && countLoaded < loadPerTick; i++) {
				boolean visible = true;
				if(!search.equals(""))
					visible = segments.get(i).contains(search);
				if(visible)
				{
					ArrayList<GuiControl> guiControls = segments.get(i).createGuiControls(this, 0, height, 220);
					ArrayList<ContainerControl> containerControls = segments.get(i).createContainerControls(0, height, 220);
					
					segments.get(i).guiControls = guiControls;
					segments.get(i).containerControls = containerControls;
					
					segments.get(i).onSegmentLoaded(0, height, 220);
					
					for (int j = 0; j < guiControls.size(); j++) {
						box.addControl(guiControls.get(j));
					}
					for (int j = 0; j < containerControls.size(); j++) {
						containerControls.get(j).parent = container;
						GuiControl control = containerControls.get(j).getGuiControl();
						box.addControl(control);
						segments.get(i).guiControls.add(control);
					}
					
					height = segments.get(i).getHeight()+5;
					
					countLoaded++;
				}
				
				count++;
				//box.addControl(new GuiAvatarButton(tab.branches.get(i).name, 5, 5+i*20, 155, 20, i, tab.branches.get(i).avatar));
			}
			
			//System.out.println("Loaded " + countLoaded + "/" + count + " segments=" + segments.size());
			
			index += count;
			if(index >= segments.size())
			{
				box.maxScroll += 5;
				get("Save").setEnabled(true);
				segments = null;
				index = 0;
			}
		}
		
	}
	
	@Override
	public boolean closeGuiUsingEscape()
	{
		if(!Minecraft.getMinecraft().isSingleplayer())
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				
				@Override
				public void run() {
					PacketHandler.sendPacketToServer(new RequestInformationPacket(branch));
				}
			});
		return true;
	}
	
	@Override
	public void createControls() {
		GuiScrollBox box = new GuiScrollBox("scrollbox", 0, 0, 244, 220);
		controls.add(box);
		controls.add(new GuiButton("Cancel", 0, 228, 50) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				closeGuiUsingEscape();
				if(branch.tab.branches.size() > 1)
					IGCM.openModOverviewGui(container.player, branch.tab.title);
				else if(branch.tab.branches.size() == 1)
					IGCM.openModsGui(container.player);
			}
		});
		controls.add((GuiControl) new GuiButton("Save", 194, 228, 50) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				IGCM.sendUpdatePacket(branch);
			}
		}.setEnabled(false));
		if(branch.isSearchable())
			controls.add(new GuiTextfield("search", "", 60, 228, 124, 14));
		createSegmentControls();
	}
	
	public GuiSlotControl openedSlot;
	
	@CustomEventSubscribe
	public void onButtonClicked(GuiControlClickEvent event)
	{
		if(event.source instanceof GuiSlotControl){
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
				SubGuiFullItemDialog dialog = new SubGuiFullItemDialog(branch.doesInputSupportStackSize());
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
	public void onTextfieldChanged(GuiControlChangedEvent event)
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

}
