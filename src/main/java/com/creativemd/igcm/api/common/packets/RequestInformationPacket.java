package com.creativemd.igcm.api.common.packets;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.igcm.api.common.branch.ConfigBranch;
import com.creativemd.igcm.api.core.IGCM;
import com.typesafe.config.Config;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class RequestInformationPacket extends CreativeCorePacket{
	
	public RequestInformationPacket()
	{
		this.id = "";
	}
	
	public String id;
	
	public RequestInformationPacket(ConfigBranch branch)
	{
		this.id = branch.name;
	}
	
	@Override
	public void writeBytes(ByteBuf buf) {
		writeString(buf, id);
	}

	@Override
	public void readBytes(ByteBuf buf) {
		id = readString(buf);
	}

	@Override
	public void executeClient(EntityPlayer player) {
		
	}

	@Override
	public void executeServer(EntityPlayer player) {
		ConfigBranch branch = ConfigBranch.getBranchByID(id);
		if(branch != null)
		{
			IGCM.sendUpdatePacket(branch, player);
		}else{
			IGCM.sendAllUpdatePackets(player);
		}
	}

}
