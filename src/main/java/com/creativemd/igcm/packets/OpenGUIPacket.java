package com.creativemd.igcm.packets;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.igcm.IGCM;
import com.creativemd.igcm.IGCMGuiManager;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class OpenGUIPacket extends CreativeCorePacket {
	
	public OpenGUIPacket() {
		
	}
	
	@Override
	public void writeBytes(ByteBuf buf) {
		
	}

	@Override
	public void readBytes(ByteBuf buf) {
		
	}

	@Override
	public void executeClient(EntityPlayer player) {
		
	}

	@Override
	public void executeServer(EntityPlayer player) {
		if(IGCM.gui.checkPermission(player.getServer(), player))
			IGCMGuiManager.openConfigGui(player);
	}

}
