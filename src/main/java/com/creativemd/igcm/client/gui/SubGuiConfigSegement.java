package com.creativemd.igcm.client.gui;

import java.util.ArrayList;
import java.util.Collection;

import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.gui.ContainerControl;
import com.creativemd.creativecore.gui.GuiControl;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.controls.container.SlotControlNoSync;
import com.creativemd.creativecore.gui.controls.container.client.GuiSlotControl;
import com.creativemd.creativecore.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.gui.event.gui.GuiControlClickEvent;
import com.creativemd.igcm.IGCM;
import com.creativemd.igcm.IGCMGuiManager;
import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.ConfigSegment;
import com.creativemd.igcm.api.ConfigTab;
import com.creativemd.igcm.container.controls.InfoSlotControl;
import com.creativemd.igcm.packets.RequestInformationPacket;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class SubGuiConfigSegement extends SubGui {
	
	public static final int loadPerTick = 20;
	
	public SubGuiConfigSegement(ConfigSegment element) {
		super(250, 250);
		this.element = element;
	}
	
	public int aimedScrollPos = -1;
	public int index;
	public int height;
	public ConfigSegment element;
	public ArrayList<ConfigSegment> childs;
	
	public boolean forceRecreation = true;
	public boolean isCreatingControls = false;
	
	public static int maxWidth = 220;
	
	public String search = "";
	
	public void removeSegment(ConfigSegment segment)
	{
		if(!isCreatingControls)
		{
			int indexOf = childs.indexOf(segment);
			if(indexOf != -1)
			{
				childs.remove(indexOf);
				
				GuiScrollBox box = (GuiScrollBox) get("scrollbox");
				box.controls.clear();
				box.maxScroll = 0;
				box.scrolled = 0;
				
				if(get("Save") != null)
					get("Save").setEnabled(false);
				
				height = 5;
				index = 0;
				
				isCreatingControls = true;
				forceRecreation = false;
				onTick();
			}
		}
	}
	
	public void createSegmentControls(boolean force)
	{
		forceRecreation = force;
		GuiScrollBox box = (GuiScrollBox) get("scrollbox");
		Collection<ConfigSegment> segments = element.getChilds();
		aimedScrollPos = box.aimedScrolled;
		box.controls.clear();
		box.maxScroll = 0;
		box.scrolled = 0;
		
		if(get("Save") != null)
			get("Save").setEnabled(false);
		
		this.childs = new ArrayList<ConfigSegment>(segments);
		height = 5;
		index = 0;
		
		if(element instanceof ConfigBranch)
			((ConfigBranch) element).onGuiCreatesSegments(this, childs);
		
		isCreatingControls = true;
		
		onTick();
	}
	
	@Override
	public void onTick()
	{
		if(childs != null && (isCreatingControls || index < childs.size()))
		{
			isCreatingControls = true;
			GuiScrollBox box = (GuiScrollBox) get("scrollbox");
			int count = 0;
			int countLoaded = 0;
			String search = this.search.toLowerCase();
			for (int i = index; i < childs.size() && countLoaded < loadPerTick; i++) {
				if(search.equals("") || childs.get(i).canBeFound(search))
				{
					ConfigSegment child = childs.get(i);
					int x = 0;
					int y = height;
					
					if(forceRecreation || child.getGuiControls() == null)
					{
						ArrayList<GuiControl> guiControls = child.createGuiControls(this, x, y, maxWidth);
						ArrayList<ContainerControl> containerControls = child.createContainerControls(container, x, y, maxWidth);
						
						child.setGuiControls(guiControls);
						child.setContainerControls(containerControls);
						
						child.onSegmentLoaded(x, y, maxWidth);
						
						for (int j = 0; j < guiControls.size(); j++) {
							box.addControl(guiControls.get(j));
						}
						for (int j = 0; j < containerControls.size(); j++) {
							containerControls.get(j).parent = container;
							GuiControl control = containerControls.get(j).getGuiControl();
							box.addControl(control);
							child.getGuiControls().add(control);
						}
						
						if(element instanceof ConfigBranch)
							((ConfigBranch) element).onGuiLoadSegment(this, box, childs, child);
					}else{
						for (int j = 0; j < child.getGuiControls().size(); j++) {
							GuiControl control = child.getGuiControls().get(j);
							control.posX = control.posX-child.lastX+x;
							control.posY = control.posY-child.lastY+y;
							box.addControl(control);
						}
						child.onSegmentLoaded(x, y, maxWidth);
					}
					
					height += child.getHeight()+5;
					
					countLoaded++;
				}
				
				count++;
			}
			
			if(aimedScrollPos != -1)
			{
				box.scrolled = Math.min(height, aimedScrollPos);
				if(height >= aimedScrollPos)
					aimedScrollPos = -1;
			}
			
			index += count;
			if(index >= childs.size())
			{
				if(element instanceof ConfigBranch)
				{
					if(has("Save"))
						get("Save").setEnabled(true);
					((ConfigBranch) element).onGuiLoadedAllSegments(this, box, childs);
					isCreatingControls = false;
				}
			}
		}
		
	}
	
	@Override
	public boolean closeGuiUsingEscape()
	{
		if(element instanceof ConfigBranch)
		{
			if(!Minecraft.getMinecraft().isSingleplayer())
				Minecraft.getMinecraft().addScheduledTask(new Runnable() {
					
					@Override
					public void run() {
						PacketHandler.sendPacketToServer(new RequestInformationPacket((ConfigBranch) element));
					}
				});
		}
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
				if(element.parent != null)
					IGCMGuiManager.openConfigGui(getPlayer(), element.parent.getPath());
				else
					closeGui();
			}
		});
		if(element instanceof ConfigBranch)
		{
			controls.add((GuiControl) new GuiButton("Save", 194, 228, 50) {
				
				@Override
				public void onClicked(int x, int y, int button) {
					SubGuiConfigSegement parent = (SubGuiConfigSegement) getParent();
					for (int i = 0; i < parent.childs.size(); i++) {
						parent.childs.get(i).saveFromControls();
					}
					if(element instanceof ConfigBranch)
					{
						((ConfigBranch) element).onGuiSavesSegments((SubGuiConfigSegement) getParent(), parent.childs);
						IGCM.sendUpdatePacket((ConfigBranch) element);
					}
				}
			}.setEnabled(false));
		}
		controls.add(new GuiTextfield("search", "", 60, 228, 124, 14));
		if(element == ConfigTab.root)
		{
			controls.add(new GuiButton("Profiles", 194, 228, 50) {
				@Override
				public void onClicked(int x, int y, int button)
				{
					IGCMGuiManager.openProfileGui(container.player);
				}
			});
		}
		createSegmentControls(true);
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
			((InfoSlotControl)openedSlot.slot).putInfo(((SubGuiFullItemDialog) gui).info);
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
		if(!(element instanceof ConfigBranch))
			return null;
		if(gui == null && nbt.getBoolean("ItemDialog"))
		{
			if(nbt.getBoolean("fullEdit"))
			{
				SubGuiFullItemDialog dialog = new SubGuiFullItemDialog(((ConfigBranch) element).doesInputSupportStackSize());
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
			
			//Temporarily
			createSegmentControls(false);
		}
	}
	
}
