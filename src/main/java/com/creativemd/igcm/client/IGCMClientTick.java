package com.creativemd.igcm.client;

import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.packet.gui.OpenGuiPacket;
import com.creativemd.creativecore.gui.mc.ContainerSub;
import com.creativemd.igcm.IGCM;
import com.creativemd.igcm.IGCMGuiManager;
import com.creativemd.igcm.packets.OpenGUIPacket;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class IGCMClientTick {
	
	public Minecraft mc = Minecraft.getMinecraft();
	
	@SubscribeEvent
	public void openGui(RenderTickEvent event)
	{
		if(mc.player != null && mc.world != null)
		{
			if(mc.gameSettings.isKeyDown(IGCMClient.openConfig) && mc.player != null && mc.inGameHasFocus && !(mc.player.openContainer instanceof ContainerSub))
				PacketHandler.sendPacketToServer(new OpenGUIPacket());
		}
	}
	
}
