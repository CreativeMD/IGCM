package com.creativemd.igcm.packets;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.igcm.IGCM;
import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.ConfigTab;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class RequestInformationPacket extends CreativeCorePacket {
	
	public RequestInformationPacket() {
	}
	
	public String path;
	
	public RequestInformationPacket(ConfigBranch branch) {
		this.path = branch.getPath();
	}
	
	@Override
	public void writeBytes(ByteBuf buf) {
		writeString(buf, path);
	}
	
	@Override
	public void readBytes(ByteBuf buf) {
		path = readString(buf);
	}
	
	@Override
	public void executeClient(EntityPlayer player) {
		
	}
	
	@Override
	public void executeServer(EntityPlayer player) {
		ConfigBranch branch = (ConfigBranch) ConfigTab.getSegmentByPath(path);
		if (branch != null) {
			IGCM.sendUpdatePacket(branch, player);
		} else {
			IGCM.sendAllUpdatePackets(player);
		}
	}
	
}