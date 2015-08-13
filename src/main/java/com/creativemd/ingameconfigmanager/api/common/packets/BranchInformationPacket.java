package com.creativemd.ingameconfigmanager.api.common.packets;

import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;
import com.creativemd.ingameconfigmanager.api.common.segment.ConfigSegment;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BranchInformationPacket extends CreativeCorePacket{
	
	public ConfigBranch branch;
	
	public int segStart;
	public int segEnd;
	
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
		buf.writeInt(branch.id);
		buf.writeInt(segStart);
		buf.writeInt(segEnd);
		
		ArrayList<ConfigSegment> segments = branch.getConfigSegments();
		for (int i = segStart; i < segEnd; i++) {
			ByteBufUtils.writeUTF8String(buf, segments.get(i).createPacketInformation());
		}
	}

	@Override
	public void readBytes(ByteBuf buf) {
		branch = ConfigBranch.branches.get(buf.readInt());
		segStart = buf.readInt();
		segEnd = buf.readInt();
		
		ArrayList<ConfigSegment> segments = branch.getConfigSegments();
		for (int i = segStart; i < segEnd; i++) {
			String information = ByteBufUtils.readUTF8String(buf);
			segments.get(i).receivePacketInformation(information);
			
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void executeClient(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeServer(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

}
