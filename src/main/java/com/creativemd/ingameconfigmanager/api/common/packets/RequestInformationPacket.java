package com.creativemd.ingameconfigmanager.api.common.packets;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.ingameconfigmanager.api.common.branch.ConfigBranch;
import com.creativemd.ingameconfigmanager.api.core.IGCM;
import com.typesafe.config.Config;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class RequestInformationPacket extends CreativeCorePacket{
	
	public RequestInformationPacket()
	{
		this.id = -1;
	}
	
	public int id;
	
	public RequestInformationPacket(ConfigBranch branch)
	{
		this.id = branch.id;
	}
	
	@Override
	public void writeBytes(ByteBuf buf) {
		buf.writeInt(id);
	}

	@Override
	public void readBytes(ByteBuf buf) {
		id = buf.readInt();
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
