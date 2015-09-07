package com.creativemd.ingameconfigmanager.api.common.packets;

import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import com.creativemd.creativecore.common.container.ContainerSub;
import com.creativemd.creativecore.common.gui.GuiContainerSub;
import com.creativemd.creativecore.common.gui.controls.GuiScrollBox;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.ingameconfigmanager.api.client.gui.SubGuiBranch;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigSegmentCollection;
import com.creativemd.ingameconfigmanager.api.common.segment.ConfigSegment;
import com.creativemd.ingameconfigmanager.api.core.InGameConfigManager;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BranchInformationPacket extends CreativeCorePacket{
	
	public ConfigBranch branch;
	public int segStart;
	public int segEnd;
	public boolean finalPacket;
	
	public BranchInformationPacket()
	{
		
	}
	
	public BranchInformationPacket(int id, int segStart, int segEnd)
	{
		this(ConfigBranch.branches.get(id), segStart, segEnd);
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
		
		buf.writeInt(branch.id);
		//buf.writeInt(segStart);
		//buf.writeInt(segEnd);
		int count = 0;
		ArrayList<ConfigSegment> segments = branch.getConfigSegments();
		for (int i = segStart; i < segEnd; i++) {
			if(segments.get(i).createPacketInformation() != null)
				count++;
		}
		
		buf.writeBoolean(segEnd == segments.size());
		buf.writeBoolean(segStart == 0);
		buf.writeInt(count);
		
		for (int i = segStart; i < segEnd; i++) {
			String input = segments.get(i).createPacketInformation();
			if(input != null)
			{
				writeString(buf, segments.get(i).getID());
				writeString(buf, input);
			}
		}
	}
	
	public ConfigSegmentCollection collection;
	
	@Override
	public void readBytes(ByteBuf buf) {
		branch = ConfigBranch.branches.get(buf.readInt());
		finalPacket = buf.readBoolean();
		boolean firstPacket = buf.readBoolean();
		int count = buf.readInt();
		
		ArrayList<ConfigSegment> segments = branch.getConfigSegments();
		if(firstPacket)
			branch.onBeforeReceived(FMLCommonHandler.instance().getEffectiveSide().isServer());
		collection = new ConfigSegmentCollection(branch.getConfigSegments());
		
		for (int i = 0; i < count; i++) {
			String id = readString(buf);
			ConfigSegment segment = collection.getSegmentByID(id);
			String information = readString(buf);
			if(segment != null)
				segment.receivePacketInformation(information);
			else
				branch.onFailedLoadingSegment(id, information);
			
		}
	}
	
	public boolean isFinalPacket()
	{
		return finalPacket;
	}
	
	public void receiveUpdate(boolean server)
	{
		
		branch.onRecieveFromPre(false, collection);
		branch.onRecieveFrom(false, collection);
		branch.onRecieveFromPost(false, collection);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void executeClient(EntityPlayer player) {
		if(isFinalPacket())
		{
			receiveUpdate(false);
			
			if(player.openContainer instanceof ContainerSub && ((ContainerSub) player.openContainer).gui.getTopLayer() instanceof SubGuiBranch)
			{
				SubGuiBranch gui = (SubGuiBranch) ((ContainerSub) player.openContainer).gui.getTopLayer();
				if(gui.branch == branch)
				{
					int scrolled = ((GuiScrollBox) gui.getControl("scrollbox")).scrolled;
					gui.createSegmentControls();
					((GuiScrollBox) gui.getControl("scrollbox")).scrolled = scrolled;
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
			
			InGameConfigManager.sendUpdatePacket(branch);
			InGameConfigManager.saveConfig(branch);
		}
	}

}
