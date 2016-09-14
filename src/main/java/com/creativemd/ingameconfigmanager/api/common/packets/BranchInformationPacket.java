package com.creativemd.ingameconfigmanager.api.common.packets;

import java.util.ArrayList;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.creativecore.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.gui.mc.ContainerSub;
import com.creativemd.ingameconfigmanager.api.client.gui.SubGuiBranch;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigSegmentCollection;
import com.creativemd.ingameconfigmanager.api.common.segment.ConfigSegment;
import com.creativemd.ingameconfigmanager.api.core.IGCM;
import com.creativemd.ingameconfigmanager.api.jei.JEIHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BranchInformationPacket extends CreativeCorePacket{
	
	public ConfigBranch branch;
	public int segStart;
	public int segEnd;
	public boolean finalPacket;
	
	public BranchInformationPacket()
	{
		
	}
	
	public BranchInformationPacket(String id, int segStart, int segEnd)
	{
		this(ConfigBranch.getBranchByID(id), segStart, segEnd);
	}
	
	public BranchInformationPacket(ConfigBranch branch, int segStart, int segEnd)
	{
		this.branch = branch;
		this.segStart = segStart;
		this.segEnd = segEnd;
	}
	
	@Override
	public void writeBytes(ByteBuf buf) {
		branch.onPacketSend(FMLCommonHandler.instance().getEffectiveSide().isServer(), new ConfigSegmentCollection(branch.getConfigSegments()));
		
		writeString(buf, branch.name);;
		//buf.writeInt(segStart);
		//buf.writeInt(segEnd);
		int count = 0;
		ArrayList<ConfigSegment> segments = branch.getConfigSegments();
		for (int i = segStart; i < segEnd; i++) {
			if(segments.size() > i && segments.get(i).createPacketInformation(FMLCommonHandler.instance().getEffectiveSide().isServer()) != null)
				count++;
		}
		
		buf.writeBoolean(segEnd == segments.size());
		buf.writeBoolean(segStart == 0);
		buf.writeInt(count);
		
		for (int i = segStart; i < segEnd; i++) {
			if(segments.size() > i)
			{
				String input = segments.get(i).createPacketInformation(FMLCommonHandler.instance().getEffectiveSide().isServer());
				if(input != null)
				{
					//System.out.println("Sending id=" + segments.get(i).getID() + ",input=" + input);
					writeString(buf, segments.get(i).getID());
					writeString(buf, input);
				}
			}
		}
		
		//System.out.println("End of sending packet number:" + segStart/10);
	}
	
	public ConfigSegmentCollection collection;
	
	@Override
	public void readBytes(ByteBuf buf) {
		//System.out.println("Receiving new packet");
		branch = ConfigBranch.getBranchByID(readString(buf));
		finalPacket = buf.readBoolean();
		boolean firstPacket = buf.readBoolean();
		int count = buf.readInt();
		
		branch.getConfigSegments();
		if(firstPacket)
			branch.onBeforeReceived(FMLCommonHandler.instance().getEffectiveSide().isServer());
		collection = new ConfigSegmentCollection(branch.getConfigSegments());
		ConfigSegmentCollection collectionOld = new ConfigSegmentCollection(new ArrayList<>(branch.getConfigSegments()));
		
		for (int i = 0; i < count; i++) {
			String id = readString(buf);
			ConfigSegment segment = collectionOld.getSegmentByID(id);
			String information = readString(buf);
			//System.out.println("Receiving id=" + id + ",input=" + information);
			if(segment != null)
				segment.receivePacketInformation(information);
			else{
				ConfigSegment fSegment = branch.onFailedLoadingSegment(collection, id, information, i);
				if(fSegment != null)
				{
					fSegment.receivePacketInformation(information);
					collection.asList().add(fSegment);
				}
			}
			
		}
		//System.out.println("End of receiving packet number:" + segStart/10);
	}
	
	public boolean isFinalPacket()
	{
		return finalPacket;
	}
	
	public void receiveUpdate(boolean server)
	{
		
		branch.onRecieveFromPre(server, collection);
		branch.onRecieveFrom(server, collection);
		branch.onRecieveFromPost(server, collection);
		
		JEIHandler.forceReload();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void executeClient(EntityPlayer player) {
		if(isFinalPacket())
		{
			receiveUpdate(false);
			
			if(player!= null && player.openContainer instanceof ContainerSub && ((ContainerSub) player.openContainer).gui.getTopLayer() instanceof SubGuiBranch)
			{
				SubGuiBranch gui = (SubGuiBranch) ((ContainerSub) player.openContainer).gui.getTopLayer();
				if(gui.branch == branch)
				{
					int scrolled = ((GuiScrollBox) gui.get("scrollbox")).scrolled;
					gui.createSegmentControls();
					GuiScrollBox box = (GuiScrollBox) gui.get("scrollbox");
					box.scrolled = scrolled;
					if(box.scrolled > box.maxScroll)
						box.scrolled = box.maxScroll;
				}
				//InGameConfigManager.openBranchGui(player, branch);
			}
		}
	}

	@Override
	public void executeServer(EntityPlayer player) {
		if(isFinalPacket())
		{
			receiveUpdate(true);
			
			IGCM.sendUpdatePacket(branch);
			IGCM.saveConfig(branch);
		}
	}

}
